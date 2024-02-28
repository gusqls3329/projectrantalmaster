package com.team5.projrental.mypage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team5.projrental.administration.enums.DisputeKind;
import com.team5.projrental.administration.model.DisputeByAdminVo;
import com.team5.projrental.administration.model.inner.DisputeInfoByAdmin;
import com.team5.projrental.board.BoardRepository;
import com.team5.projrental.common.exception.thrid.ClientException;
import com.team5.projrental.common.model.ResVo;
import com.team5.projrental.common.security.AuthenticationFacade;
import com.team5.projrental.dispute.repository.DisputeBoardRepository;
import com.team5.projrental.dispute.repository.DisputeChatUserRepository;
import com.team5.projrental.dispute.repository.DisputePaymentRepository;
import com.team5.projrental.dispute.repository.DisputeProductRepository;
import com.team5.projrental.entities.*;
import com.team5.projrental.entities.enums.DisputeStatus;
import com.team5.projrental.entities.inheritance.Users;
import com.team5.projrental.mypage.model.*;
import com.team5.projrental.payment.thirdproj.PaymentRepository;
import com.team5.projrental.product.thirdproj.japrepositories.product.ProductRepository;
import com.team5.projrental.user.UserRepository;
import com.team5.projrental.user.UsersRepository;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.team5.projrental.common.exception.ErrorMessage.ILLEGAL_EX_MESSAGE;
import static com.team5.projrental.entities.QDisputeProduct.disputeProduct;
import static com.team5.projrental.entities.inheritance.QUsers.users;

@Service
@RequiredArgsConstructor
public class MypageService {
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final UsersRepository usersRepository;
    private final MyPageBoardRepository myPageBoardRepository;


    private final DisputeBoardRepository disputeBoardRepository;
    private final DisputePaymentRepository disputePaymentRepository;
    private final DisputeProductRepository disputeProductRepository;
    private final DisputeChatUserRepository disputeChatUserRepository;

    private final MyPageDisputeRepository myPageDisputeRepository;
    private final MypageMapper mapper;
    private final AuthenticationFacade authenticationFacade;

    public List<BuyPaymentSelVo> getRentalList(PaymentSelDto dto) {
        Long loginUserPk = authenticationFacade.getLoginUserPk();
        dto.setLoginedIuser(loginUserPk);
        if (dto.getRole() == 1) {
            List<BuyPaymentSelVo> list = mapper.getPaymentList(loginUserPk);
            return list;
        }
        if (dto.getRole() == 2) {
            List<BuyPaymentSelVo> list = mapper.getProductList(loginUserPk);
            return list;
        }

        throw new ClientException(ILLEGAL_EX_MESSAGE);
    }

    public List<MyFavListSelVo> getFavList(MyFavListSelDto dto) {
        Long loginUserPk = authenticationFacade.getLoginUserPk();
        dto.setIuser(loginUserPk);
        List<MyFavListSelVo> vo = mapper.getFavList(dto);
        return vo;
    }


    public List<MyBuyReviewListSelVo> getReview(MyBuyReviewListSelDto dto) {
        Long loginUserPk = authenticationFacade.getLoginUserPk();
        dto.setIuser(loginUserPk);

        List<MyBuyReviewListSelVo> list = mapper.getIbuyerReviewList(dto);
        return list;
    }

    /*public List<MyBuyReviewListSelVo> getReview(MyBuyReviewListSelDto dto) {
        return null;
                //mapper.getAllReviewFromMyProduct(authenticationFacade.getLoginUserPk());
    }*/

    public DisputeByMyPageVo getDispute(MyBuyReviewListSelDto dto) {
        Long loginUserPk = authenticationFacade.getLoginUserPk();
        dto.setIuser(loginUserPk);
        List<Dispute> disputes = myPageDisputeRepository.getDisputeList(loginUserPk);
        List<MyDisputeVo> myDisputeVo = disputes.stream().map(dispute -> {
            DisputeKind kind = null;
            DisputeBoard disputeBoard = null;
            DisputePayment disputePayment = null;
            DisputeProduct disputeProduct = null;
            DisputeChatUser disputeChat = null;
            if (dispute instanceof DisputeBoard) {
                disputeBoard = (DisputeBoard) dispute;
                kind = DisputeKind.BOARD;
            }

            Optional<DisputeBoard> board = disputeBoardRepository.findById(dispute.getId());

            if (dispute instanceof DisputePayment) {
                disputePayment = (DisputePayment) dispute;
                kind = DisputeKind.PAYMENT;
            }
            Optional<DisputePayment> payment = disputePaymentRepository.findById(dispute.getId());

            if (dispute instanceof DisputeProduct) {
                disputeProduct = (DisputeProduct) dispute;
                kind = DisputeKind.PRODUCT;
            }
            Optional<DisputeProduct> product = disputeProductRepository.findById(dispute.getId());

            if (dispute instanceof DisputeChatUser) {
                disputeChat = (DisputeChatUser) dispute;
                kind = DisputeKind.CHAT;
            }
            Optional<DisputeChatUser> chatUser = disputeChatUserRepository.findById(dispute.getId());

            Optional<User> user = userRepository.findById(loginUserPk);
            return MyDisputeVo.builder()
                    .idispute(dispute.getId())
                    .ireporter(user.get().getId())
                    .ireportedUser(dispute.getReportedUser().getId())
                    .category(dispute.getReason())
                    .details(dispute.getDetails())
                    .penality(dispute.getPenalty())
                    .createdAt(LocalDate.from(dispute.getCreatedAt()))
                    .status(dispute.getStatus())
                    .kind(kind != null ? kind.getNum() : null)
                    .nick(dispute.getReportedUser().getNick())
                    .profPic(dispute.getReportedUser().getBaseUser().getStoredPic())
                    .iproduct(kind == DisputeKind.PRODUCT ? product.get().getProduct().getId() : null)
                    .tilte(kind == DisputeKind.PRODUCT ? product.get().getProduct().getTitle() : null)
                    .prodPic(kind == DisputeKind.PRODUCT ? product.get().getProduct().getStoredPic() : null)
                    .ipayment(kind == DisputeKind.PAYMENT ? payment.get().getId() : null)
                    .code(kind == DisputeKind.PAYMENT ? payment.get().getPaymentInfo().getCode() : null)
                    .ichat(kind == DisputeKind.CHAT ? chatUser.get().getId() : null)
                    .lastMsgAt(kind == DisputeKind.CHAT ? chatUser.get().getChatUser().getChat().getLastMsgAt() : null)
                    .lastMsg(kind == DisputeKind.CHAT ? chatUser.get().getChatUser().getChat().getLastMsg() : null)
                    .iboard(kind == DisputeKind.BOARD ? board.get().getBoard().getId() : null)
                    .boardTitle(kind == DisputeKind.BOARD ? board.get().getBoard().getTitle() : null)
                    .build();
        }).toList();
        return DisputeByMyPageVo.builder().disputes(myDisputeVo).build();

    }

    public ResVo cancelDispute(Long idispute) {
        Long loginUserPk = authenticationFacade.getLoginUserPk();
        Optional<Dispute> dispute = myPageDisputeRepository.findById(idispute);
        if (dispute.isEmpty() || dispute.get().getReportedUser().getId() != loginUserPk) {
            throw new ClientException(ILLEGAL_EX_MESSAGE);
        }
        if (dispute.get().getStatus() == DisputeStatus.ACCEPTED || dispute.get().getStatus() == DisputeStatus.CANCELED) {
            throw new ClientException(ILLEGAL_EX_MESSAGE);
        }

        dispute.get().setStatus(DisputeStatus.CANCELED);
        return null;
    }

    public MyBoardListVo getBoard(BoardDto dto) {
        Long loginUserPk = authenticationFacade.getLoginUserPk();
        Optional<User> user = userRepository.findById(loginUserPk);

        List<Board> boards = myPageBoardRepository.findByUser(user.get(), dto.getPage());

        return MyBoardListVo.builder().list(boards.stream().map(
                board -> MyBoardVo.builder()
                        .iboard(board.getId().intValue())
                        .title(board.getTitle())
                        .createdAt(board.getCreatedAt().toString())
                        .build()
        ).toList(
        )).build();
    }


}
