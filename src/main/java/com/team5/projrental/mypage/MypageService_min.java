package com.team5.projrental.mypage;

import com.team5.projrental.administration.enums.DisputeKind;
import com.team5.projrental.common.Const;
import com.team5.projrental.common.exception.thrid.ClientException;
import com.team5.projrental.common.model.ResVo;
import com.team5.projrental.common.security.AuthenticationFacade;
import com.team5.projrental.dispute.repository.DisputeBoardRepository;
import com.team5.projrental.dispute.repository.DisputeChatUserRepository;
import com.team5.projrental.dispute.repository.DisputePaymentRepository;
import com.team5.projrental.dispute.repository.DisputeProductRepository;
import com.team5.projrental.entities.*;
import com.team5.projrental.entities.enums.DisputeStatus;
import com.team5.projrental.mypage.model.*;
import com.team5.projrental.payment.thirdproj.PaymentRepository;
import com.team5.projrental.product.thirdproj.japrepositories.product.ProductRepository;
import com.team5.projrental.user.UserRepository;
import com.team5.projrental.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.team5.projrental.common.exception.ErrorMessage.ILLEGAL_EX_MESSAGE;

@Service
@RequiredArgsConstructor
public class MypageService_min {

    private final MyPageDisputeRepository myPageDisputeRepository;
    private final AuthenticationFacade authenticationFacade;


    //


    @Transactional
    public DisputeByMyPageVo getDispute(MyBuyReviewListSelDto dto) {


        Long loginUserPk = authenticationFacade.getLoginUserPk();

        Long totalCount = myPageDisputeRepository.totalCountByOptions(loginUserPk);
        List<Dispute> findDisputes = myPageDisputeRepository.getDisputeList(loginUserPk);

        return DisputeByMyPageVo.builder()
                .totalUserCount(totalCount)
                .disputes(
                        findDisputes.stream()
                                .map(dispute -> {
                                    MyDisputeVo vo = MyDisputeVo.builder()
                                            .idispute(dispute.getId())
                                            .ireporter(dispute.getReporter().getId())
                                            .category(dispute.getReason())
                                            .details(dispute.getDetails())
                                            .createdAt(dispute.getCreatedAt().toLocalDate())
                                            .status(dispute.getStatus())
                                            .penalty(dispute.getPenalty())
                                            .ireportedUser(dispute.getReportedUser().getId())
                                            .reportedUserNick(dispute.getReportedUser().getNick())
                                            .build();

                                    if (dispute instanceof DisputeProduct) {
                                        DisputeProduct disputeProduct = (DisputeProduct) dispute;
                                        vo.setTilte(disputeProduct.getProduct().getTitle());

                                        vo.setKind(DisputeKind.PRODUCT.getNum());
                                        vo.setPk(disputeProduct.getProduct().getId());
                                    }
                                    if (dispute instanceof DisputePayment) {
                                        DisputePayment disputePayment = (DisputePayment) dispute;
                                        vo.setCode(disputePayment.getPaymentInfo().getPayment().getCode());
                                        vo.setKind(DisputeKind.PAYMENT.getNum());
                                        vo.setPk(disputePayment.getPaymentInfo().getPayment().getId());
                                    }
                                    if (dispute instanceof DisputeChatUser) {
                                        DisputeChatUser disputeChatUser = (DisputeChatUser) dispute;
                                        vo.setLastMsg(disputeChatUser.getChatUser().getChat().getLastMsg());
                                        vo.setLastMsgAt(disputeChatUser.getChatUser().getChat().getLastMsgAt());
                                        vo.setKind(DisputeKind.CHAT.getNum());
                                        vo.setPk(disputeChatUser.getChatUser().getChat().getId());

                                    }
                                    if (dispute instanceof DisputeBoard) {
                                        DisputeBoard disputeBoard = (DisputeBoard) dispute;
                                        vo.setBoardTitle(disputeBoard.getBoard().getTitle());
                                        vo.setKind(DisputeKind.BOARD.getNum());
                                        vo.setPk(disputeBoard.getBoard().getId());

                                    }
                                    return vo;
                                })
                                .collect(Collectors.toList()))
                .build();
    }

    //
}
