package com.team5.projrental.administration;

import com.team5.projrental.admin.AdminRepository;
import com.team5.projrental.administration.enums.DisputeKind;
import com.team5.projrental.administration.model.*;
import com.team5.projrental.administration.model.inner.*;
import com.team5.projrental.administration.repository.*;
import com.team5.projrental.common.Const;
import com.team5.projrental.common.exception.ErrorCode;
import com.team5.projrental.common.exception.thrid.ClientException;
import com.team5.projrental.common.model.ResVo;
import com.team5.projrental.common.security.AuthenticationFacade;
import com.team5.projrental.common.sse.enums.SseCode;
import com.team5.projrental.common.sse.enums.SseKind;
import com.team5.projrental.common.sse.model.SseMessageInfo;
import com.team5.projrental.dispute.repository.DisputeChatUserRepository;
import com.team5.projrental.entities.*;
import com.team5.projrental.entities.enums.*;
import com.team5.projrental.payment.thirdproj.paymentinfo.PaymentInfoRepository;
import com.team5.projrental.payment.thirdproj.refund.RefundRepository;
import com.team5.projrental.product.thirdproj.japrepositories.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdministrationService {
    private final ApplicationEventPublisher eventPublisher;

    private final PaymentInfoRepository paymentInfoRepository;
    private final AdminUserRepository userRepository;
    private final AdminRepository adminRepository;
    private final AuthenticationFacade facade;
    private final AdminBoardRepository boardRepository;
    private final ResolvedBoardRepository resolvedBoardRepository;
    private final ResolvedUserRepository resolvedUserRepository;
    private final ResolvedProductRepository resolvedProductRepository;
    private final AdminDisputeRepository adminDisputeRepository;
    private final ProductRepository productRepository;
    private final RefundRepository refundRepository;
    private final DisputeChatUserRepository disputeChatUserRepository;
    private final AdminChatRepository chatRepository;

    @Transactional
    public PaymentByAdminVo getPayment(String code) {

        // user, product, payment, paymentInfo
        // user = 로그인 유져

        PaymentInfo findPaymentInfo = paymentInfoRepository.findJoinFetchPaymentProductByUser(code).orElseThrow(() -> new ClientException(ErrorCode.BAD_INFO_EX_MESSAGE,
                "잘못된 코드입니다."));

        return PaymentByAdminVo.builder()
                .iproduct(findPaymentInfo.getPayment().getProduct().getId())
                .title(findPaymentInfo.getPayment().getProduct().getTitle())
                .prodMainPic(findPaymentInfo.getPayment().getProduct().getStoredPic())

                .ipayment(findPaymentInfo.getPayment().getId())
                .rentalStartDate(findPaymentInfo.getPayment().getRentalDates().getRentalStartDate().toLocalDate())
                .rentalEndDate(findPaymentInfo.getPayment().getRentalDates().getRentalEndDate().toLocalDate())
                .rentalDuration(findPaymentInfo.getPayment().getDuration())
                .totalPrice(findPaymentInfo.getPayment().getTotalPrice())
                .deposit(findPaymentInfo.getPayment().getDeposit())
                .createdAt(findPaymentInfo.getPayment().getCreatedAt())

                .iuser(findPaymentInfo.getUser().getId())
                .myPaymentCode(findPaymentInfo.getCode())
                .nick(findPaymentInfo.getUser().getNick())
                .phone(findPaymentInfo.getUser().getPhone())
                .userStoredPic(findPaymentInfo.getUser().getBaseUser().getStoredPic())
                .userRating(findPaymentInfo.getUser().getBaseUser().getRating())
                .status(findPaymentInfo.getUser().getStatus())

                .method(findPaymentInfo.getPayment().getPaymentDetail().getCategory())
                .build();

    }


    @Transactional
    public ResVo delBoard(Long iboard, Integer reason) {

        Board findBoard = boardRepository.findActivatedById(iboard).orElseThrow(() -> new ClientException(ErrorCode.ILLEGAL_EX_MESSAGE,
                "해당 게시물이 존재하지 않습니다."));

        ResolvedBoard saveResolvedBoard = ResolvedBoard.builder()
                .admin(adminRepository.findById(getLoginUserPk()).orElseThrow(() -> new ClientException(ErrorCode.SERVER_ERR_MESSAGE,
                        "운영자 정보가 잘못되었습니다.")))
                .board(findBoard)
                .reason(DisputeReason.getByNum(reason))
                .build();
        User findUser = findBoard.getUser();
        findUser.setPenalty((byte) (findUser.getPenalty() + DisputeReason.getByNum(reason).getPenaltyScore()));

        if (findUser.getPenalty() <= -50) changeUserWhenOverPenalty(
                findUser,
                adminRepository.findById(getLoginUserPk()).orElseThrow(() -> new ClientException(ErrorCode.NO_SUCH_USER_EX_MESSAGE,
                        "운영자 정보가 잘못되었습니다.")),
                DisputeReason.DELETE_BY_ADMIN);

        findBoard.setStatus(BoardStatus.DELETED);

        resolvedBoardRepository.save(saveResolvedBoard);
        resolvedBoardRepository.flush();

        // sse 이벤트 발행
        eventPublisher.publishEvent(SseMessageInfo.builder()
                .receiver(findBoard.getUser().getId())
                .kind(SseKind.BOARD.getNum())
                .code(SseCode.BOARD_DELETED_BY_ADMIN.getNum())
                .identityNum(findBoard.getId())
                .description("운영자에 의해 자유게시판 게시물이 삭제됨.")
                .build());

        return new ResVo(Const.SUCCESS);
    }

    @Transactional
    public UserByAdminVo getAllUsers(Integer page, Integer searchType, String search, Integer status) {
        List<User> findUser = userRepository.findUserByOptions(
                page,
                searchType,
                search == null ? "" : search,
                status == null || status == 1 ? null :
                        status == 2 ? UserStatus.ACTIVATED :
                                status == 3 ? UserStatus.DELETED : null);

        return UserByAdminVo.builder()
                .totalUserCount((long) findUser.size())
                .users(findUser.stream()
                        .map(user -> UserInfoByAdmin.builder()
                                .iuser(user.getId())
                                .uid(user.getUid())
                                .nick(user.getNick())
                                .createdAt(user.getCreatedAt())
                                .email(user.getEmail())
                                .penalty((int) user.getPenalty())
                                .status(user.getStatus())
                                .build()).collect(Collectors.toList())
                )
                .build();
    }

    @Transactional
    public ResVo delUser(Long iuser, Integer reason) {

        User findUser = userRepository.findById(iuser).orElseThrow(() -> new ClientException(ErrorCode.NO_SUCH_USER_EX_MESSAGE,
                "존재하지 않는 유저입니다."));
        Admin thisAdmin = adminRepository.findById(getLoginUserPk()).orElseThrow(() -> new ClientException(ErrorCode.NO_SUCH_USER_EX_MESSAGE,
                "운영자 정보가 잘못되었습니다."));
        ResolvedUser saveResolvedUser = ResolvedUser.builder()
                .admin(thisAdmin)
                .user(findUser)
                .reason(DisputeReason.getByNum(reason))
                .build();
        changeUserWhenOverPenalty(
                findUser,
                thisAdmin,
                DisputeReason.getByNum(reason)
        );
        findUser.setStatus(UserStatus.DELETED);

        resolvedUserRepository.save(saveResolvedUser);
        resolvedUserRepository.flush();

        eventPublisher.publishEvent(SseMessageInfo.builder()
                .receiver(findUser.getId())
                .kind(SseKind.USER.getNum())
                .code(SseCode.USER_DELETED_BY_ADMIN.getNum())
                .identityNum(findUser.getId())
                .description("운영자에 의해 유저가 삭제됨.")
                .build());

        return new ResVo(findUser.getId());
    }

    public DisputeByAdminVo getAllDispute(int page, Integer div, String search, Integer category, Integer status) {
        List<Dispute> findDisputes = adminDisputeRepository.findByLimitPage(page, div, search, category, status);
        long count = findDisputes.size();
        List<DisputeInfoByAdmin> disputeInfoByAdmins = findDisputes.stream().map(dispute -> {
            DisputeKind kind = null;
            String pk = "";
            if (dispute instanceof DisputeBoard) {
                DisputeBoard disputeBoard = (DisputeBoard) dispute;
                kind = DisputeKind.BOARD;
                pk = String.valueOf(disputeBoard.getBoard().getId());
            }
            if (dispute instanceof DisputePayment) {
                DisputePayment disputePayment = (DisputePayment) dispute;
                kind = DisputeKind.PAYMENT;
                pk = "ipayment: " + disputePayment.getPaymentInfo().getPaymentInfoIds().getIpayment()
                     + ", iuser: " + disputePayment.getPaymentInfo().getPaymentInfoIds().getIuser();
            }
            if (dispute instanceof DisputeProduct) {
                DisputeProduct disputeProduct = (DisputeProduct) dispute;
                kind = DisputeKind.PRODUCT;
                pk = String.valueOf(disputeProduct.getProduct().getId());
            }
            if (dispute instanceof DisputeChatUser) {
                DisputeChatUser disputeChat = (DisputeChatUser) dispute;
                kind = DisputeKind.CHAT;
                pk = String.valueOf(disputeChat.getChatUser().getId());
            }
            return DisputeInfoByAdmin.builder()
                    .idispute(dispute.getId())
                    .ireportedUser(dispute.getReportedUser().getId())
                    .uid(dispute.getReportedUser().getUid())
                    .nick(dispute.getReportedUser().getNick())
                    .category(dispute.getReason())
                    .createdAt(dispute.getCreatedAt())
                    .ireporter(dispute.getReporter().getId())
                    .status(dispute.getStatus())
                    .penalty((int) dispute.getPenalty())
                    .kind(kind != null ? kind.name() : null)
                    .pk(pk)
                    .build();
        }).toList();

        return DisputeByAdminVo.builder()
                .totalDisputeCount(count)
                .disputes(disputeInfoByAdmins)
                .build();
    }

    @Transactional
    public ResVo postDispute(Long idispute, Integer type) {
        Dispute findDispute =
                adminDisputeRepository.findByIdAndStatus(idispute, DisputeStatus.STAND_BY).orElseThrow(() -> new ClientException(ErrorCode.ILLEGAL_EX_MESSAGE,
                        "식별숫자에 해당하는 신고가 존재하지 않습니다."));
        DisputeStatus changeDisputeStatus = DisputeStatus.getByNum(type);
        findDispute.setStatus(changeDisputeStatus);

        User penaltyUser = findDispute.getReportedUser();
        penaltyUser.setPenalty((byte) (penaltyUser.getPenalty() + findDispute.getPenalty()));

        if (penaltyUser.getPenalty() <= -50) {

            changeUserWhenOverPenalty(penaltyUser, adminRepository.findById(getLoginUserPk()).orElseThrow(() -> new ClientException(ErrorCode.NO_SUCH_USER_EX_MESSAGE,
                    "운영자 정보가 잘못되었습니다.")), DisputeReason.DELETE_BY_ADMIN);
        }

        eventPublisher.publishEvent(SseMessageInfo.builder()
                .receiver(findDispute.getReporter().getId())
                .description(String.format("당신이 신고한 신고가 %s되었습니다.",
                        changeDisputeStatus == DisputeStatus.ACCEPTED ? "수리" : "반려"))
                .code(changeDisputeStatus == DisputeStatus.ACCEPTED ? DisputeStatus.ACCEPTED.getNum() :
                        DisputeStatus.DENIED.getNum())
                .identityNum(idispute)
                .kind(SseKind.DISPUTE.getNum())
                .build());

        eventPublisher.publishEvent(SseMessageInfo.builder()
                .receiver(findDispute.getReportedUser().getId())
                .description(String.format("당신을 신고한 신고가 %s되었습니다.",
                        changeDisputeStatus == DisputeStatus.ACCEPTED ? "수리" : "반려"))
                .code(changeDisputeStatus == DisputeStatus.ACCEPTED ? DisputeStatus.ACCEPTED.getNum() :
                        DisputeStatus.DENIED.getNum())
                .identityNum(idispute)
                .kind(SseKind.DISPUTE.getNum())
                .build());


        return new ResVo((long) changeDisputeStatus.getNum());
    }

    public ProductByAdminVo getAllProducts(int page, Integer type, String search, Integer sort) {
        // 삭제된 상태의 제품은 제외하고 조회


        List<Product> findProducts = productRepository.findAllLimitPage(page, type, search, sort);
        // 카테고리는 db에서 조회하는 방법이 없어서 자바에서 수행. (limit 이 있으므로 성능상 엄청 크게 손해는 없다.)
        if (type != null && type == 2) {
            findProducts = findProducts.stream().filter(p -> {
                String basName = p.getSubCategory().getName();

                return basName.contains(search) ||
                       basName.equalsIgnoreCase(search);

            }).toList();
        }
        long count = findProducts.size();
        List<ProductInfoByAdmin> productResults = findProducts.stream().map(product -> ProductInfoByAdmin.builder()
                .iproduct(product.getId())
                .mainCategory(product.getMainCategory().getNum())
                .subCategory(product.getSubCategory().getNum())
                .pricePerDay(product.getRentalPrice())
                .view(product.getView())
                .createdAt(product.getCreatedAt())
                .status(product.getStatus().getNum())

                .iuser(product.getUser().getId())
                .nick(product.getUser().getNick())
                .build()).toList();

        return ProductByAdminVo.builder()
                .totalDisputeCount(count)
                .products(productResults)
                .build();
    }

    @Transactional
    public ResVo patchProduct(Long iproduct, Integer reason) {
        Product findProduct =
                productRepository.findByIdJoinFetch(iproduct).orElseThrow(() -> new ClientException(ErrorCode.NO_SUCH_PRODUCT_EX_MESSAGE,
                        "잘못된 제품 식별 번호입니다. (iproduct)"));


        findProduct.setStatus(ProductStatus.DELETED);

        Admin thisAdmin = adminRepository.findById(getLoginUserPk()).orElseThrow(() -> new ClientException(ErrorCode.NO_SUCH_USER_EX_MESSAGE,
                "운영자 정보가 잘못되었습니다."));
        ResolvedProduct saveResolvedProduct = ResolvedProduct.builder()
                .admin(thisAdmin)
                .product(findProduct)
                .reason(DisputeReason.getByNum(reason))
                .build();

        resolvedProductRepository.save(saveResolvedProduct);
        resolvedProductRepository.flush();
        eventPublisher.publishEvent(SseMessageInfo.builder()
                .receiver(findProduct.getUser().getId())
                .description("운영자에 의해 제품 게시글이 삭제됨.")
                .code(SseCode.PRODUCT_DELETE_BY_ADMIN.getNum())
                .identityNum(findProduct.getId())
                .kind(SseKind.PRODUCT.getNum())
                .build());

        return new ResVo(1L);
    }

    @Transactional
    public ResVo patchRefund(Long irefund, Integer div) {

        Refund findRefund = refundRepository.findById(irefund).orElseThrow(() -> new ClientException(ErrorCode.ILLEGAL_EX_MESSAGE,
                "잘못된 식별숫자 입니다. (irefund)"));

        ResVo result = new ResVo((long) findRefund.getRefundAmount());
        if (div == 1) {
            result.setResult((long) -1);
            findRefund.setRefundAmount(0);
        }
        findRefund.setStatus(RefundStatus.getByNum(div));
        findRefund.setAdmin(adminRepository.findById(getLoginUserPk()).orElseThrow(() -> new ClientException(ErrorCode.NO_SUCH_USER_EX_MESSAGE,
                "운영자 정보가 잘못되었습니다.")));
        return result;
    }

    @Transactional
    public BoardByAdminVo getAllBoards(int page, Integer type, String search, Integer sort) {
        List<Board> findBoards = boardRepository.findAllLimitPage(page, type, search, sort);

        return BoardByAdminVo.builder()
                .totalBoardCount((long) findBoards.size())
                .boards(findBoards.stream().map(board -> BoardInfoByAdmin.builder()
                        .iboard(board.getId())
                        .nick(board.getUser().getNick())
                        .view(board.getView())
                        .createdAt(board.getCreatedAt())
                        .build()).collect(Collectors.toList()))
                .build();

    }

    public RefundByAdminVo getAllRefunds(Integer page, Integer status) {
        List<Refund> findRefunds = refundRepository.findAllLimitPage(page, status);
        return RefundByAdminVo.builder()
                .totalRefundCount((long) findRefunds.size())
                .refunds(findRefunds.stream().map(refund -> RefundInfoByAdmin.builder()
                        .irefund(refund.getId())
                        .iuser(refund.getUser().getId())
                        .uid(refund.getUser().getUid())
                        .nick(refund.getUser().getNick())
                        .amount(refund.getRefundAmount())
                        .status(refund.getStatus().getNum())
                        .createdAt(refund.getCreatedAt())
                        .updatedAt(refund.getUpdatedAt())
                        .iadmin(refund.getAdmin().getId())
                        .adminUid(refund.getAdmin().getUid())
                        .build()).collect(Collectors.toList()))
                .build();
    }

    public ChatByAdminVo getAllChats(Integer page) {

        List<ChatUser> findChatUser = disputeChatUserRepository.findAllLimitPage(page);
        return ChatByAdminVo.builder()
                .totalChatCount((long) findChatUser.size())
                .chats(findChatUser.stream().map(chatUser -> ChatInfoByAdmin.builder()
                        .ichat(chatUser.getChat().getId())
                        .category(chatUser.getChat().getProduct().getSubCategory().name())
                        .nick(chatUser.getUser().getNick())
                        .createdAt(chatUser.getChat().getCreatedAt())
                        .build()).collect(Collectors.toList()))
                .build();

    }

    /* ------- Extracted Methods ------- */

    @Transactional
    protected void changeUserWhenOverPenalty(User user, Admin admin, DisputeReason reason) {
        user.setStatus(UserStatus.DELETED);
        ResolvedUser saveResolvedUser = ResolvedUser.builder()
                .admin(admin)
                .user(user)
                .reason(reason)
                .build();
        resolvedUserRepository.save(saveResolvedUser);
    }

    private Long getLoginUserPk() {
        return facade.getLoginUserPk();
    }


    @Transactional
    public ResVo delChat(Long ichat) {
        Chat findChat = chatRepository.findById(ichat).orElseThrow(() -> new ClientException(ErrorCode.ILLEGAL_EX_MESSAGE,
                "잘못된 식별 번호 입니다. (ichat)"));
        chatRepository.delete(findChat);

        return new ResVo(1L);
    }
}
