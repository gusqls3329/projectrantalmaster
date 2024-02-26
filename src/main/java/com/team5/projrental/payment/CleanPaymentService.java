//package com.team5.projrental.payment;
//
//import com.team5.projrental.common.Const;
//import com.team5.projrental.common.Flag;
//import com.team5.projrental.common.Role;
//import com.team5.projrental.common.aop.anno.NamedLock;
//import com.team5.projrental.common.exception.*;
//import com.team5.projrental.common.exception.base.BadDateInfoException;
//import com.team5.projrental.common.exception.base.BadInformationException;
//import com.team5.projrental.common.exception.base.WrapRuntimeException;
//import com.team5.projrental.common.exception.thrid.ClientException;
//import com.team5.projrental.common.model.ResVo;
//import com.team5.projrental.common.security.AuthenticationFacade;
//import com.team5.projrental.common.utils.CommonUtils;
//import com.team5.projrental.entities.*;
//import com.team5.projrental.entities.embeddable.RentalDates;
//import com.team5.projrental.entities.enums.PaymentInfoStatus;
//import com.team5.projrental.entities.enums.ProductStatus;
//import com.team5.projrental.entities.ids.PaymentInfoIds;
//import com.team5.projrental.payment.model.PaymentInsDto;
//import com.team5.projrental.payment.model.PaymentVo;
//import com.team5.projrental.payment.model.proc.*;
//import com.team5.projrental.payment.thirdproj.PaymentRepository;
//import com.team5.projrental.payment.thirdproj.paymentdetail.PaymentDetailRepository;
//import com.team5.projrental.payment.thirdproj.paymentinfo.PaymentInfoRepository;
//import com.team5.projrental.payment.thirdproj.refund.RefundRepository;
//import com.team5.projrental.product.ProductMybatisRepository;
//import com.team5.projrental.product.thirdproj.japrepositories.product.ProductRepository;
//import com.team5.projrental.user.UserRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.temporal.ChronoUnit;
//import java.util.Objects;
//import java.util.UUID;
//
//import static com.team5.projrental.common.exception.ErrorCode.*;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class CleanPaymentService implements RefPaymentService {
//
//    private final PaymentMybatisRepository paymentMybatisRepository;
//    private final ProductMybatisRepository productMybatisRepository;
//    private final AuthenticationFacade authenticationFacade;
//    private final UserRepository userRepository;
//    private final PaymentDetailRepository paymentDetailRepository;
//    private final ProductRepository productRepository;
//    private final PaymentRepository paymentRepository;
//    private final PaymentInfoRepository paymentInfoRepository;
//    private final RefundRepository refundRepository;
//
//    //    @Transactional
//    @NamedLock("postPayment")
//    public ResVo postPayment(PaymentInsDto paymentInsDto) {
//
//        // EndDate 가 StartDate 보다 이전이면 예외
//        if (paymentInsDto.getRentalEndDate().isBefore(paymentInsDto.getRentalStartDate())) {
//            throw new ClientException(RENTAL_END_DATE_MUST_BE_AFTER_THAN_RENTAL_START_DATE_EX_MESSAGE,
//                    "잘못된 대여 날짜");
//        }
//        if (paymentInsDto.getRentalStartDate().isBefore(LocalDate.now())) {
//            throw new ClientException(RENTAL_DATE_MUST_BE_BEFORE_THAN_TODAY_EX_MESSAGE,
//                    "잘못된 대여 날짜");
//        }
//
//
//        Long loginUserPk = getLoginUserPk();
//
//        User findUserBuyer = userRepository.findById(loginUserPk).orElseThrow(() -> new ClientException(NO_SUCH_USER_EX_MESSAGE,
//                "해당하는 정보의 유저가 존재하지 않음 (로그인관련)"));
//
//        PaymentDetail findPaymentDetail = paymentDetailRepository.findById(paymentInsDto.getPaymentInfoId()).orElseThrow(() -> new ClientException(NO_SUCH_PAYMENT_EX_MESSAGE,
//                "조회된 결제 이력이 없음. (paymentDetailId"));
//
//        Product findProduct =
//                productRepository.findByIdJoinStock(paymentInsDto.getIproduct(), ProductStatus.ACTIVATED).orElseThrow(() -> new ClientException(NO_SUCH_PRODUCT_EX_MESSAGE,
//                        "조회된 상품 정보가 없음. (iproduct)"));
//
//        // 거래 시작일이 제품의 거래시작일짜보다 이전이거나, 거래 종료일이 제품의 거래종료일짜보다 이후면 예외 발생
//        CommonUtils.ifBeforeThrow(BadDateInfoException.class, ILLEGAL_DATE_EX_MESSAGE,
//                paymentInsDto.getRentalStartDate(), findProduct.getRentalDates().getRentalStartDate().toLocalDate());
//        CommonUtils.ifAfterThrow(BadDateInfoException.class, ILLEGAL_DATE_EX_MESSAGE,
//                findProduct.getRentalDates().getRentalEndDate().toLocalDate(), paymentInsDto.getRentalEndDate());
//
//        int rentalDuration =
//                (int) (ChronoUnit.DAYS.between(paymentInsDto.getRentalStartDate(), paymentInsDto.getRentalEndDate()) + 1);
//        int totalPriceExcludeDeposit = rentalDuration * paymentInsDto.getPricePerDate();
//
//        Payment savePayment = Payment.builder()
//                .paymentDetail(findPaymentDetail)
//                .stock(findProduct.getStocks().get(0))
//                .product(findProduct)
//                .user(findUserBuyer)
//                .rentalDates(RentalDates.builder()
//                        .rentalStartDate(LocalDateTime.of(paymentInsDto.getRentalStartDate(), LocalTime.of(0, 0, 0)))
//                        .rentalEndDate(LocalDateTime.of(paymentInsDto.getRentalEndDate(), LocalTime.of(23, 59, 59)))
//                        .build())
//                .duration(rentalDuration)
//                // 전체 결제금액임 -> 따라서 보증금을 분리해주어야 함.
//                .totalPrice(totalPriceExcludeDeposit)
//                .deposit(paymentInsDto.getTotalPrice() - totalPriceExcludeDeposit)
//                .code(createCode())
//                .build();
//
//        paymentRepository.save(savePayment);
//
//        PaymentInfo buyerPaymentInfo = PaymentInfo.builder()
//                .paymentInfoIds(PaymentInfoIds.builder()
//                        .iusers(findUserBuyer.getId())
//                        .ipayment(savePayment.getId())
//                        .build())
//                .payment(savePayment)
//                .status(paymentInsDto.getRentalStartDate().isAfter(LocalDate.now()) ? PaymentInfoStatus.RESERVED : PaymentInfoStatus.ACTIVATED)
//                .code(createPaymentInfoCode())
//                .user(findUserBuyer)
//                .build();
//
//        PaymentInfo sellerPaymentInfo = PaymentInfo.builder()
//                .paymentInfoIds(PaymentInfoIds.builder()
//                        .iusers(findUserBuyer.getId())
//                        .ipayment(savePayment.getId())
//                        .build())
//                .payment(savePayment)
//                .status(paymentInsDto.getRentalStartDate().isAfter(LocalDate.now()) ? PaymentInfoStatus.RESERVED : PaymentInfoStatus.ACTIVATED)
//                .code(createPaymentInfoCode())
//                .user(findProduct.getUser())
//                .build();
//        paymentInfoRepository.save(buyerPaymentInfo);
//        paymentInfoRepository.save(sellerPaymentInfo);
//
//
//        return new ResVo(Const.SUCCESS);
//
//    }
//
//
//    /**
//     * div == 3
//     * -3: ex (이미 완료된 거래)
//     * -2: ex (이미 완료된 거래)
//     * -1: ex (이미 완료된 거래)
//     * 0: do (2 or 3)
//     * 1: ex (이미 완료된 거래)
//     * 2: do or ex (요청자가 구매자인 경우만 -3)
//     * 3: do or ex (요청자가 판매자인 경우만 -3)
//     * <p>
//     * div == 2
//     * -3: do
//     * -2: ex (이미 숨김처리 됨)
//     * -1: ex (삭제된 상품을 숨길 수 없음)
//     * 0: ex (거래중이면 숨길수 없음)
//     * 1: do
//     * 2: ex (거래중이면 숨길수 없음)
//     * 3: ex (거래중이면 숨길수 없음)
//     * <p>
//     * div == 1
//     * -3: do
//     * -2: do
//     * -1: ex (이미 삭제된 결제)
//     * 0: ex (거래중이면 삭제 불가능)
//     * 1: do
//     * 2: ex (거래중이면 삭제 불가능)
//     * 3: ex (거래중이면 삭제 불가능)
//     *
//     * @param ipayment
//     * @param div
//     * @return ResVo
//     */
//    @Transactional
//    public ResVo delPayment(Long ipayment, Integer div) {
//        // 데이터 검증
//        Long loginUserPk = getLoginUserPk();
//
//        PaymentInfo findPaymentInfo = paymentInfoRepository.findById(PaymentInfoIds.builder()
//                .ipayment(ipayment)
//                .iusers(loginUserPk)
//                .build()).orElseThrow(() -> new ClientException(NO_SUCH_PAYMENT_EX_MESSAGE,
//                "잘못된 결제정보"));
//
//        if (div == 1 && !(findPaymentInfo.getStatus() == PaymentInfoStatus.COMPLETED ||
//                          findPaymentInfo.getStatus() == PaymentInfoStatus.HIDDEN ||
//                          findPaymentInfo.getStatus() == PaymentInfoStatus.CANCELED)) {
//            throw new ClientException(ErrorCode.ILLEGAL_EX_MESSAGE, "삭제할 수 없는 상태입니다.");
//        }
//        if (div == 2 && !(findPaymentInfo.getStatus() == PaymentInfoStatus.COMPLETED ||
//                          findPaymentInfo.getStatus() == PaymentInfoStatus.CANCELED)) {
//            throw new ClientException(ErrorCode.ILLEGAL_EX_MESSAGE, "숨길 수 없는 상태입니다.");
//        }
//        if (div == 3 && !(findPaymentInfo.getStatus() == PaymentInfoStatus.RESERVED)) {
//            throw new ClientException(ErrorCode.ILLEGAL_EX_MESSAGE, "취소할수 없는 상태입니다.");
//        }
//        // product fetch join 을 위해 별도로 repository 에서 가져오기
//        Payment findPayment = paymentRepository.findByIdJoinFetchProduct(ipayment);
//        User findBuyer = findPayment.getUser();
//        User findSeller = findPayment.getProduct().getUser();
//
//        boolean isBuyer = Objects.equals(findPayment.getId(), loginUserPk);
//
//        findPaymentInfo.setStatus(
//                div == 1 ? PaymentInfoStatus.DELETED :
//                        div == 2 ? PaymentInfoStatus.HIDDEN :
//                                PaymentInfoStatus.CANCELED
//        );
//
//        if (findPaymentInfo.getStatus() == PaymentInfoStatus.RESERVED && div == 3) {
//
//            int basePrice = findPayment.getTotalPrice() + findPayment.getDeposit();
//            long dateGap = ChronoUnit.DAYS.between(LocalDate.now(),
//                    findPayment.getRentalDates().getRentalStartDate().toLocalDate()) + 1;
//            double penaltyPer = (dateGap <= 3 ? 70 : dateGap <= 7 ? 50 : 0) * 0.1;
//            if (isBuyer) {
//                // 만약 예약취소 요청자가 구매자인경우
//                int refundForSeller = (int) (basePrice * penaltyPer);
//                int refundForBuyer = basePrice - refundForSeller;
//
//                Refund refundSeller = getRefundEntity(findSeller, refundForSeller, findPayment);
//                Refund refundBuyer = getRefundEntity(
//                        findBuyer,
//                        refundForBuyer + findPayment.getDeposit()
//                        , findPayment);
//
//                refundRepository.save(refundSeller);
//                refundRepository.save(refundBuyer);
//            }
//            if (!isBuyer) {
//                Refund refundEntity = getRefundEntity(findBuyer,
//                        basePrice + findPayment.getDeposit(),
//                        findPayment);
//                refundRepository.save(refundEntity);
//            }
//        }
//        return new ResVo((long) -findPaymentInfo.getStatus().getNum());
//    }
//
//
//
//
//    public PaymentVo getPayment(Long ipayment) {
//
//        paymentRepository.findById(ipayment);
//
//        // 가져오기
//        // iuser 또는 ipayment 가 없으면 결과가 size 0 일 것
//        GetPaymentListResultDto aPayment = paymentMybatisRepository.findPaymentBy(
//                new GetPaymentListDto(getLoginUserPk(), Flag.ONE.getValue(), ipayment));
//        CommonUtils.ifAllNotNullThrow(NoSuchPaymentException.class, NO_SUCH_PAYMENT_EX_MESSAGE);
//        return new PaymentVo(aPayment);
//    }
//
//    /*
//    ------- Extracted Method -------
//    */
//    private String createCode() {
//
//        String systemCurrent = String.valueOf(System.currentTimeMillis());
//        String uuidBackBase = UUID.randomUUID().toString();
//
//        String genCode = uuidBackBase.substring(0, 4) +
//                         systemCurrent +
//                         uuidBackBase.substring(uuidBackBase.length() - 3);
//        if (paymentRepository.findByCode(genCode) == null) {
//            return genCode;
//        }
//        return createCode();
//
//    }
//
//    /**
//     * 0: 활성화 상태
//     * 1: 거래 완료됨
//     * <p>
//     * -1: 삭제됨
//     * -2: 숨김
//     * -3: 취소됨
//     * -4: 기간지남
//     * <p>
//     * 논리적 삭제 가능 숫자: 1, -2, -3
//     * 숨김 가능 숫자: 1, -3
//     * 취소 요청시:
//     * Role.buyer ->
//     */
//    private Integer divResolver(Integer div, Integer istatus, Role role) {
//
//        if (role == null) throw new BadInformationException(BAD_INFO_EX_MESSAGE);
//        if (istatus == 3 && role == Role.BUYER || istatus == 2 && role == Role.SELLER)
//            // 이미 취소한 상태.
//            throw new BadDivInformationException(BAD_DIV_INFO_EX_MESSAGE);
//        if (div == 1 || div == 2) {
//            if (istatus == -3 || istatus == 1 || (div == 1 && istatus == -2)) {
//                return div * -1;
//            }
//        }
//        if (div == 3) {
//            if (istatus == 0) {
////                if (role == Role.BUYER) {
////                    return 3;
////                }
////                if (role == Role.SELLER) {
////                    return 2;
////                }
////            }
////            if (istatus == 2 && role == Role.BUYER || istatus == 3 && role == Role.SELLER) {
//                return div * -1;
//            }
//        }
//        throw new BadDivInformationException(BAD_DIV_INFO_EX_MESSAGE);
//    }
//
//    private Long getLoginUserPk() {
//        return authenticationFacade.getLoginUserPk();
//    }
//
//    private String createPaymentInfoCode() {
//        String code = createCode().substring(0, 6);
//
//        if (paymentInfoRepository.findByCode(code) == null) {
//            return code;
//        }
//        return createPaymentInfoCode();
//    }
//
//    private static Refund getRefundEntity(User user, int refundAmount, Payment payment) {
//        return Refund.builder()
//                .user(user)
//                .refundAmount(refundAmount)
//                .payment(payment)
//                .build();
//    }
//}
