package com.team5.projrental.dispute;

import com.team5.projrental.board.BoardRepository;
import com.team5.projrental.common.exception.ErrorCode;
import com.team5.projrental.common.exception.thrid.ClientException;
import com.team5.projrental.common.model.ResVo;
import com.team5.projrental.common.security.AuthenticationFacade;
import com.team5.projrental.dispute.model.DisputeDto;
import com.team5.projrental.dispute.repository.*;
import com.team5.projrental.entities.*;
import com.team5.projrental.entities.enums.DisputeReason;
import com.team5.projrental.entities.enums.DisputeStatus;
import com.team5.projrental.entities.enums.PaymentInfoStatus;
import com.team5.projrental.payment.thirdproj.paymentinfo.PaymentInfoRepository;
import com.team5.projrental.product.thirdproj.japrepositories.product.ProductRepository;
import com.team5.projrental.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DisputeService {

    private final AuthenticationFacade facade;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final DisputeProductRepository disputeProductRepository;
    private final DisputeUserRepository disputeUserRepository;
    private final ChatUserRepository chatUserRepository;
    private final DisputeChatUserRepository disputeChatUserRepository;
    private final BoardRepository boardRepository;
    private final DisputeBoardRepository disputeBoardRepository;
    private final PaymentInfoRepository paymentInfoRepository;
    private final DisputePaymentRepository disputePaymentRepository;


    @Transactional
    public ResVo postDisputeProduct(DisputeDto dto) {

        User reporter = userRepository.findById(getLoginUserPk())
                .orElseThrow(() -> new ClientException(
                        ErrorCode.NO_SUCH_USER_EX_MESSAGE,
                        "로그인 유저 정보가 올바르지 않음.")
                );

        Product findProduct = productRepository.findJoinFetchById(dto.getIdentity())
                .orElseThrow(() -> new ClientException(
                        ErrorCode.ILLEGAL_EX_MESSAGE,
                        "잘못된 제품 정보 (identity)")
                );

        User reportedUser = findProduct.getUser();

        DisputeProduct saveDisputeProduct = getDisputeBaseEntity(
                reporter, reportedUser,
                DisputeReason.getByNum(dto.getReason()),
                dto.getDetails(), new DisputeProduct()
        );

        saveDisputeProduct.setProduct(findProduct);

        disputeProductRepository.save(saveDisputeProduct);

        return new ResVo(1L);
    }

    @Transactional
    public ResVo postDisputeUser(DisputeDto dto) {
        User reporter = userRepository.findById(getLoginUserPk())
                .orElseThrow(() -> new ClientException(
                        ErrorCode.NO_SUCH_USER_EX_MESSAGE,
                        "로그인 유저 정보가 올바르지 않음.")
                );

        User reportedUser = userRepository.findById(dto.getIdentity())
                .orElseThrow(() -> new ClientException(
                        ErrorCode.ILLEGAL_EX_MESSAGE,
                        "잘못된 유저 정보 (identity)")
                );

        DisputeUser saveDisputeUser = getDisputeBaseEntity(
                reporter, reportedUser,
                DisputeReason.getByNum(dto.getReason()),
                dto.getDetails(), new DisputeUser()
        );

        saveDisputeUser.setUser(reportedUser);

        disputeUserRepository.save(saveDisputeUser);

        return new ResVo(1L);
    }

    @Transactional
    public ResVo postDisputeChat(DisputeDto dto) {
        User reporter = userRepository.findById(getLoginUserPk())
                .orElseThrow(() -> new ClientException(
                        ErrorCode.NO_SUCH_USER_EX_MESSAGE,
                        "로그인 유저 정보가 올바르지 않음.")
                );
        ChatUser findChatUser = chatUserRepository.findByIchatAndNeUser(dto.getIdentity(), reporter)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.ILLEGAL_EX_MESSAGE,
                        "해당 채팅방이 존재하지 않거나, 채팅방에 참여하지 않은 유저입니다.")
                );
        User reportedUser = findChatUser.getUser();

        DisputeChatUser saveDisputeChatUser = getDisputeBaseEntity(
                reporter, reportedUser,
                DisputeReason.getByNum(dto.getReason()),
                dto.getDetails(), new DisputeChatUser()
        );

        saveDisputeChatUser.setChatUser(findChatUser);

        disputeChatUserRepository.save(saveDisputeChatUser);

        return new ResVo(1L);
    }

    @Transactional
    public ResVo postDisputeBoard(DisputeDto dto) {

        User reporter = userRepository.findById(getLoginUserPk())
                .orElseThrow(() -> new ClientException(
                        ErrorCode.NO_SUCH_USER_EX_MESSAGE,
                        "로그인 유저 정보가 올바르지 않음.")
                );
        Board findBoard = boardRepository.findByIdJoinFetch(dto.getIdentity())
                .orElseThrow(() -> new ClientException(
                        ErrorCode.ILLEGAL_EX_MESSAGE,
                        "해당 게시물 존재하지 않습니다.")
                );
        User reportedUser = findBoard.getUser();

        DisputeBoard saveDisputeBoard = getDisputeBaseEntity(
                reporter, reportedUser,
                DisputeReason.getByNum(dto.getReason()),
                dto.getDetails(), new DisputeBoard()
        );

        saveDisputeBoard.setBoard(findBoard);

        disputeBoardRepository.save(saveDisputeBoard);

        return new ResVo(1L);
    }

    public ResVo postDisputePayment(DisputeDto dto) {

        /*
        만약 type 이 payment 일 경우, 해당 거래의 상태가 거래시작 or 만료됨일때만 가능하다.
        예약, 예약취소, 거래완료, 삭제됨, 숨김 시에는 신고가 불가능하다.
         */
        User reporter = userRepository.findById(getLoginUserPk())
                .orElseThrow(() -> new ClientException(
                        ErrorCode.NO_SUCH_USER_EX_MESSAGE,
                        "로그인 유저 정보가 올바르지 않음.")
                );

        PaymentInfo findPaymentInfo;

        try {
            findPaymentInfo = paymentInfoRepository.findByIdJoinFetch(
                    dto.getIdentity(),
                    reporter,
                    List.of(PaymentInfoStatus.ACTIVATED, PaymentInfoStatus.EXPIRED)
            ).orElseThrow(() -> new ClientException(
                    ErrorCode.ILLEGAL_EX_MESSAGE,
                    "해당 결제 정보가 존재하지 않습니다.")
            );
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ClientException(
                    ErrorCode.ILLEGAL_EX_MESSAGE,
                    "해당 결제 정보가 존재하지 않습니다."
            );

        }

        User reportedUser = findPaymentInfo.getUser();

        DisputePayment saveDisputePayment = getDisputeBaseEntity(
                reporter, reportedUser,
                DisputeReason.getByNum(dto.getReason()),
                dto.getDetails(), new DisputePayment()
        );

        saveDisputePayment.setPaymentInfo(findPaymentInfo);

        disputePaymentRepository.save(saveDisputePayment);

        return new ResVo(1L);
    }

    /* ------ Extracted Method ------ */

    private <T extends Dispute> T getDisputeBaseEntity(User reporter,
                                                       User reportedUser,
                                                       DisputeReason reason,
                                                       String details,
                                                       T base) {
        base.setReporter(reporter);
        base.setReportedUser(reportedUser);
        base.setReason(reason);
        base.setDetails(details);
        base.setPenalty((byte) reason.getPenaltyScore());
        base.setStatus(DisputeStatus.STAND_BY);
        return base;
    }


    private Long getLoginUserPk() {
        return facade.getLoginUserPk();
    }


}
