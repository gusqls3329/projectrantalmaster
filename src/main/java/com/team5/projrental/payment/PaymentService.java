package com.team5.projrental.payment;

import com.team5.projrental.admin.model.PaymentInfoVo;
import com.team5.projrental.common.Const;
import com.team5.projrental.common.aop.anno.NamedLock;
import com.team5.projrental.common.exception.BadDivInformationException;
import com.team5.projrental.common.exception.ErrorCode;
import com.team5.projrental.common.exception.base.BadDateInfoException;
import com.team5.projrental.common.exception.base.BadInformationException;
import com.team5.projrental.common.exception.thrid.ClientException;
import com.team5.projrental.common.exception.thrid.ServerException;
import com.team5.projrental.common.model.ResVo;
import com.team5.projrental.common.security.AuthenticationFacade;
import com.team5.projrental.common.utils.CommonUtils;
import com.team5.projrental.entities.*;
import com.team5.projrental.entities.embeddable.RentalDates;
import com.team5.projrental.entities.enums.PaymentInfoStatus;
import com.team5.projrental.entities.enums.ProductStatus;
import com.team5.projrental.entities.ids.PaymentInfoIds;
import com.team5.projrental.payment.model.PaymentInsDto;
import com.team5.projrental.payment.thirdproj.PaymentRepository;
import com.team5.projrental.payment.thirdproj.paymentdetail.PaymentDetailRepository;
import com.team5.projrental.payment.thirdproj.paymentinfo.PaymentInfoRepository;
import com.team5.projrental.payment.thirdproj.refund.RefundRepository;
import com.team5.projrental.product.ProductMybatisRepository;
import com.team5.projrental.product.thirdproj.japrepositories.product.ProductRepository;
import com.team5.projrental.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

import static com.team5.projrental.common.exception.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentMybatisRepository paymentMybatisRepository;
    private final ProductMybatisRepository productMybatisRepository;
    private final AuthenticationFacade authenticationFacade;
    private final UserRepository userRepository;
    private final PaymentDetailRepository paymentDetailRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentInfoRepository paymentInfoRepository;
    private final RefundRepository refundRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @NamedLock("postPayment")
    public ResVo postPayment(PaymentInsDto paymentInsDto) {

        // EndDate 가 StartDate 보다 이전이면 예외
        if (paymentInsDto.getRentalEndDate().isBefore(paymentInsDto.getRentalStartDate())) {
            throw new ClientException(RENTAL_END_DATE_MUST_BE_AFTER_THAN_RENTAL_START_DATE_EX_MESSAGE,
                    "잘못된 대여 날짜");
        }
        if (paymentInsDto.getRentalStartDate().isBefore(LocalDate.now())) {
            throw new ClientException(RENTAL_DATE_MUST_BE_BEFORE_THAN_TODAY_EX_MESSAGE,
                    "잘못된 대여 날짜");
        }


        Long loginUserPk = getLoginUserPk();

        User findUserBuyer = userRepository.findById(loginUserPk).orElseThrow(() -> new ClientException(NO_SUCH_USER_EX_MESSAGE,
                "해당하는 정보의 유저가 존재하지 않음 (로그인관련)"));

        PaymentDetail findPaymentDetail =
                paymentDetailRepository.findById(paymentInsDto.getPaymentDetailId()).orElseThrow(() -> new ClientException(NO_SUCH_PAYMENT_EX_MESSAGE,
                        "조회된 결제 이력이 없음. (paymentDetailId"));

        Product findProduct =
                productRepository.findByIdJoinStock(paymentInsDto.getIproduct(), ProductStatus.ACTIVATED).orElseThrow(() -> new ClientException(NO_SUCH_PRODUCT_EX_MESSAGE,
                        "조회된 상품 정보가 없음. (iproduct)"));

        // 이미 해당 날짜로 예약된 상품이 있는지 확인
        if (paymentRepository.validateRentalDate(paymentInsDto.getIproduct(), paymentInsDto.getRentalStartDate(),
                paymentInsDto.getRentalEndDate())) {
            throw new ClientException(ILLEGAL_DATE_EX_MESSAGE, "이미 예약된 날짜입니다.");
        }

        // 거래 시작일이 제품의 거래시작일짜보다 이전이거나, 거래 종료일이 제품의 거래종료일짜보다 이후면 예외 발생
        CommonUtils.ifBeforeThrow(BadDateInfoException.class, ILLEGAL_DATE_EX_MESSAGE,
                paymentInsDto.getRentalStartDate(), findProduct.getRentalDates().getRentalStartDate().toLocalDate());
        CommonUtils.ifAfterThrow(BadDateInfoException.class, ILLEGAL_DATE_EX_MESSAGE,
                findProduct.getRentalDates().getRentalEndDate().toLocalDate(), paymentInsDto.getRentalEndDate());

        int rentalDuration =
                (int) (ChronoUnit.DAYS.between(paymentInsDto.getRentalStartDate(), paymentInsDto.getRentalEndDate()) + 1);
        int totalPriceExcludeDeposit = rentalDuration * paymentInsDto.getPricePerDate();
        int deposit =
                (int) (((Math.min(rentalDuration, 5) * 10 + 100) * 0.01) * paymentInsDto.getPricePerDate()) * 5;

        if (findPaymentDetail.getAmount() != totalPriceExcludeDeposit + deposit) {
            throw new ServerException(SERVER_ERR_MESSAGE, "결제금액과 제공된 금액정보가 일치하지 않음");
        }

        Payment savePayment = Payment.builder()
                .paymentDetail(findPaymentDetail)
                .stock(findProduct.getStocks().get(0))
                .product(findProduct)
                .user(findUserBuyer)
                .rentalDates(RentalDates.builder()
                        .rentalStartDate(LocalDateTime.of(paymentInsDto.getRentalStartDate(), LocalTime.of(0, 0, 0)))
                        .rentalEndDate(LocalDateTime.of(paymentInsDto.getRentalEndDate(), LocalTime.of(23, 59, 59)))
                        .build())
                .duration(rentalDuration)
                // 전체 결제금액임 -> 따라서 보증금을 분리해주어야 함.
                .totalPrice(totalPriceExcludeDeposit)
                .deposit(deposit)
                .code(createCode())
                .build();

        paymentRepository.save(savePayment);

        PaymentInfo buyerPaymentInfo = PaymentInfo.builder()
                .paymentInfoIds(PaymentInfoIds.builder()
                        .iuser(findUserBuyer.getId())
                        .ipayment(savePayment.getId())
                        .build())
                .payment(savePayment)
                .status(paymentInsDto.getRentalStartDate().isAfter(LocalDate.now()) ? PaymentInfoStatus.RESERVED : PaymentInfoStatus.ACTIVATED)
                .code(createPaymentInfoCode())
                .user(findUserBuyer)
                .build();

        PaymentInfo sellerPaymentInfo = PaymentInfo.builder()
                .paymentInfoIds(PaymentInfoIds.builder()
                        .iuser(findUserBuyer.getId())
                        .ipayment(savePayment.getId())
                        .build())
                .payment(savePayment)
                .status(paymentInsDto.getRentalStartDate().isAfter(LocalDate.now()) ? PaymentInfoStatus.RESERVED : PaymentInfoStatus.ACTIVATED)
                .code(createPaymentInfoCode())
                .user(findProduct.getUser())
                .build();
        paymentInfoRepository.save(buyerPaymentInfo);
        paymentInfoRepository.save(sellerPaymentInfo);


        return new ResVo(Const.SUCCESS);

    }


    /**
     * div == 3
     * -3: ex (이미 완료된 거래)
     * -2: ex (이미 완료된 거래)
     * -1: ex (이미 완료된 거래)
     * 0: do (2 or 3)
     * 1: ex (이미 완료된 거래)
     * 2: do or ex (요청자가 구매자인 경우만 -3)
     * 3: do or ex (요청자가 판매자인 경우만 -3)
     * <p>
     * div == 2
     * -3: do
     * -2: ex (이미 숨김처리 됨)
     * -1: ex (삭제된 상품을 숨길 수 없음)
     * 0: ex (거래중이면 숨길수 없음)
     * 1: do
     * 2: ex (거래중이면 숨길수 없음)
     * 3: ex (거래중이면 숨길수 없음)
     * <p>
     * div == 1
     * -3: do
     * -2: do
     * -1: ex (이미 삭제된 결제)
     * 0: ex (거래중이면 삭제 불가능)
     * 1: do
     * 2: ex (거래중이면 삭제 불가능)
     * 3: ex (거래중이면 삭제 불가능)
     *
     * @param ipayment
     * @param div
     * @return ResVo
     */
    @Transactional
    public ResVo delPayment(Long ipayment, Integer div) {
        // 데이터 검증
        Long loginUserPk = getLoginUserPk();

        PaymentInfo findPaymentInfo = paymentInfoRepository.findByIdJoinFetchPaymentInfoAndProductBy(PaymentInfoIds.builder()
                .ipayment(ipayment)
                .iuser(loginUserPk)
                .build()).orElseThrow(() -> new ClientException(NO_SUCH_PAYMENT_EX_MESSAGE,
                "잘못된 결제정보"));

        if (div == 1 && !(findPaymentInfo.getStatus() == PaymentInfoStatus.COMPLETED ||
                          findPaymentInfo.getStatus() == PaymentInfoStatus.HIDDEN ||
                          findPaymentInfo.getStatus() == PaymentInfoStatus.CANCELED)) {
            throw new ClientException(ErrorCode.ILLEGAL_EX_MESSAGE, "삭제할 수 없는 상태입니다.");
        }
        if (div == 2 && !(findPaymentInfo.getStatus() == PaymentInfoStatus.COMPLETED ||
                          findPaymentInfo.getStatus() == PaymentInfoStatus.CANCELED)) {
            throw new ClientException(ErrorCode.ILLEGAL_EX_MESSAGE, "숨길 수 없는 상태입니다.");
        }
        if (div == 3 && !(findPaymentInfo.getStatus() == PaymentInfoStatus.RESERVED)) {
            throw new ClientException(ErrorCode.ILLEGAL_EX_MESSAGE, "취소할수 없는 상태입니다.");
        }
        // product fetch join 을 위해 별도로 repository 에서 가져오기
        // paymentInfo 가 이미 조회된 상태다. 따라서 payment 는 무조건 존재한다.
//        Payment findPayment = paymentRepository.findByIdJoinFetchProduct(ipayment);
        // 미리 로드했다. 다만, 생각하는데로 쿼리가 한번만 나가는지는 모르겠다.
        Payment findPayment = findPaymentInfo.getPayment();
        User findBuyer = findPayment.getUser();
        User findSeller = findPayment.getProduct().getUser();

        boolean isBuyer = Objects.equals(findPayment.getId(), loginUserPk);

        findPaymentInfo.setStatus(
                div == 1 ? PaymentInfoStatus.DELETED :
                        div == 2 ? PaymentInfoStatus.HIDDEN :
                                PaymentInfoStatus.CANCELED
        );

        if (findPaymentInfo.getStatus() == PaymentInfoStatus.RESERVED && div == 3) {

            int basePrice = findPayment.getTotalPrice() + findPayment.getDeposit();
            long dateGap = ChronoUnit.DAYS.between(LocalDate.now(),
                    findPayment.getRentalDates().getRentalStartDate().toLocalDate()) + 1;
            double penaltyPer = (dateGap <= 3 ? 100 : dateGap <= 7 ? 50 : 0) * 0.1;
            if (isBuyer) {
                // 만약 예약취소 요청자가 구매자인경우
                int refundForSeller = (int) (basePrice * penaltyPer);
                int refundForBuyer = basePrice - refundForSeller;

                Refund refundSeller = getRefundEntity(findSeller, refundForSeller, findPayment);
                Refund refundBuyer = getRefundEntity(
                        findBuyer,
                        refundForBuyer + findPayment.getDeposit()
                        , findPayment
                );

                refundRepository.save(refundSeller);
                refundRepository.save(refundBuyer);
            }
            if (!isBuyer) {
                Refund refundEntity = getRefundEntity(findBuyer,
                        basePrice + findPayment.getDeposit(),
                        findPayment);
                refundRepository.save(refundEntity);
            }
        }
        return new ResVo((long) -findPaymentInfo.getStatus().getNum());
    }


    @Transactional
    public PaymentInfoVo getPaymentInfo(Long ipayment) {
        Long loginUserPk = getLoginUserPk();
        PaymentInfo findPaymentInfo = paymentInfoRepository.findByIdJoinFetchPaymentInfoAndProductBy(PaymentInfoIds.builder()
                .iuser(loginUserPk)
                .ipayment(ipayment)
                .build()).orElseThrow(() -> new ClientException(NO_SUCH_PAYMENT_EX_MESSAGE,
                "잘못된 결제정보 입니다. (login user, ipayment"));

        return PaymentInfoVo.builder()
                .method(findPaymentInfo.getPayment().getPaymentDetail().getCategory().name())
                .totalPrice(findPaymentInfo.getPayment().getTotalPrice())
                .deposit(findPaymentInfo.getPayment().getDeposit())
                .rentalStartDate(findPaymentInfo.getPayment().getRentalDates().getRentalStartDate().toLocalDate())
                .rentalEndDate(findPaymentInfo.getPayment().getRentalDates().getRentalEndDate().toLocalDate())
                .code(findPaymentInfo.getPayment().getCode())
                .ipayment(findPaymentInfo.getPayment().getId())
                .title(findPaymentInfo.getPayment().getProduct().getTitle())
                .prodMainPic(findPaymentInfo.getPayment().getProduct().getStoredPic())
                .myPaymentCode(findPaymentInfo.getCode())
                .paymentStatus(findPaymentInfo.getCode())
                .build();

    }


    @Transactional
    public ResVo patchPayInfo(String code) {
        PaymentInfo findPaymentInfo = paymentInfoRepository.findJoinFetchPaymentByCode(code).orElseThrow(() -> new ClientException(NO_SUCH_PAYMENT_EX_MESSAGE,
                "잘못된 코드입니다."));
        PaymentInfo myPaymentInfo = paymentInfoRepository.findById(PaymentInfoIds.builder()
                .ipayment(findPaymentInfo.getPayment().getId())
                .iuser(getLoginUserPk())
                .build()).orElseThrow(() -> new ClientException(NO_SUCH_PAYMENT_EX_MESSAGE,
                "결제중인 유저의 코드가 아님."));
        if (myPaymentInfo.getCode().equals(code)) {
            throw new ClientException(ILLEGAL_EX_MESSAGE, "거래 상대의 코드를 입력해 주세요.");
        }

        if (findPaymentInfo.getStatus() == PaymentInfoStatus.RESERVED && myPaymentInfo.getStatus() == PaymentInfoStatus.RESERVED) {

            // 상태 거래중으로 변경, 새로운 코드 발급
            findPaymentInfo.setStatus(PaymentInfoStatus.ACTIVATED);
            myPaymentInfo.setStatus(PaymentInfoStatus.ACTIVATED);

            do {
                findPaymentInfo.setCode(createPaymentInfoCode());
                myPaymentInfo.setCode(createPaymentInfoCode());
            } while (findPaymentInfo.getCode().equals(myPaymentInfo.getCode()));

            return new ResVo(Const.SUCCESS);
        }
        if ((findPaymentInfo.getStatus() == PaymentInfoStatus.ACTIVATED && myPaymentInfo.getStatus() == PaymentInfoStatus.ACTIVATED)
            || findPaymentInfo.getStatus() == PaymentInfoStatus.EXPIRED && myPaymentInfo.getStatus() == PaymentInfoStatus.EXPIRED) {

            // 상태 완료됨으로 변경,
            // 거래코드 null 로 변경 (차후 데이터가 지나치게 쌓여서 중복코드가 발생할 확률이 높아졌을때의 성능저하를 방지하기 위해 null 로 변경

            findPaymentInfo.setStatus(PaymentInfoStatus.COMPLETED);
            myPaymentInfo.setStatus(PaymentInfoStatus.COMPLETED);
            findPaymentInfo.setCode(null);
            myPaymentInfo.setCode(null);

            return new ResVo(Const.SUCCESS);
        }
        throw new ClientException(BAD_INFO_EX_MESSAGE, "상태를 변경할 수 없는 결제 입니다.");
    }

    /*
    ------- Extracted Method -------
    */
    private String createCode() {

        String systemCurrent = String.valueOf(System.currentTimeMillis());
        String uuidBackBase = UUID.randomUUID().toString();

        String genCode = uuidBackBase.substring(0, 4) +
                         systemCurrent +
                         uuidBackBase.substring(uuidBackBase.length() - 3);
        if (paymentRepository.findByCode(genCode) == null) {
            return genCode;
        }
        return createCode();

    }

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

    private Long getLoginUserPk() {
        return authenticationFacade.getLoginUserPk();
    }

    private String createPaymentInfoCode() {
        String code = createCode().substring(0, 6);

        if (paymentInfoRepository.findByCode(code) == null) {
            return code;
        }
        return createPaymentInfoCode();
    }

    private static Refund getRefundEntity(User user, int refundAmount, Payment payment) {
        return Refund.builder()
                .user(user)
                .refundAmount(refundAmount)
                .payment(payment)
                .build();
    }
}
