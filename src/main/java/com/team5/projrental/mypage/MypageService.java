package com.team5.projrental.mypage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team5.projrental.administration.enums.DisputeKind;
import com.team5.projrental.administration.model.DisputeByAdminVo;
import com.team5.projrental.administration.model.inner.DisputeInfoByAdmin;
import com.team5.projrental.board.BoardRepository;
import com.team5.projrental.common.Const;
import com.team5.projrental.common.exception.thrid.ClientException;
import com.team5.projrental.common.model.ResVo;
import com.team5.projrental.common.security.AuthenticationFacade;
import com.team5.projrental.dispute.repository.DisputeBoardRepository;
import com.team5.projrental.dispute.repository.DisputeChatUserRepository;
import com.team5.projrental.dispute.repository.DisputePaymentRepository;
import com.team5.projrental.dispute.repository.DisputeProductRepository;
import com.team5.projrental.entities.*;
import com.team5.projrental.entities.enums.BoardStatus;
import com.team5.projrental.entities.enums.DisputeStatus;
import com.team5.projrental.entities.enums.ProductStatus;
import com.team5.projrental.entities.inheritance.Users;
import com.team5.projrental.mypage.model.*;
import com.team5.projrental.payment.thirdproj.PaymentRepository;
import com.team5.projrental.product.thirdproj.japrepositories.product.ProductRepository;
import com.team5.projrental.user.UserRepository;
import com.team5.projrental.user.UsersRepository;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.team5.projrental.common.exception.ErrorMessage.ILLEGAL_EX_MESSAGE;

@Service
@RequiredArgsConstructor
public class MypageService {
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final UsersRepository usersRepository;
    private final MyPageBoardRepository myPageBoardRepository;
    private final MyPageBoardComRepository myPageBoardComRepository;

    private final DisputeBoardRepository disputeBoardRepository;
    private final DisputePaymentRepository disputePaymentRepository;
    private final DisputeProductRepository disputeProductRepository;
    private final DisputeChatUserRepository disputeChatUserRepository;

    private final MyPageDisputeRepository myPageDisputeRepository;
    private final MypageMapper mapper;
    private final AuthenticationFacade authenticationFacade;

    public List<BuyPaymentSelVo> getRentalList(PaymentSelDto dto) {
        Long loginUserPk = authenticationFacade.getLoginUserPk();
        dto.setIuser(loginUserPk);
        if (dto.getRole() == 1) {
            List<BuyPaymentSelVo> list = mapper.getPaymentList(dto);

            return list;
        }
        if (dto.getRole() == 2) {
            List<BuyPaymentSelVo> list = mapper.getProductList(dto);
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

    /*ublic ProdReviewListSelVo getProdReview(ProdReviewListSelDto dto) {
        Long loginUserPk = authenticationFacade.getLoginUserPk();
        dto.setIuser(loginUserPk);

    }*/

    @Transactional
    public DisputeByMyPageVo getDispute(MyBuyReviewListSelDto dto) {


        Long loginUserPk = authenticationFacade.getLoginUserPk();

        Long totalCount = myPageDisputeRepository.totalCountByOptions(loginUserPk);
        List<Dispute> findDisputes = myPageDisputeRepository.getDisputeList(loginUserPk, dto.getStartIdx(), dto.getRowCount());

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
                                            .reason(dispute.getReason().getNum())
                                            .build();

                                    if (dispute instanceof DisputeUser) {
                                        DisputeUser disputeUser = (DisputeUser) dispute;
                                        vo.setNick(disputeUser.getUser().getNick());

                                        vo.setKind(DisputeKind.USER.getNum());
                                        vo.setPk(disputeUser.getUser().getId());
                                    }
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

    @Transactional
    public ResVo cancelDispute(Long idispute) {
        Long loginUserPk = authenticationFacade.getLoginUserPk();
        Optional<Dispute> dispute = myPageDisputeRepository.findById(idispute);

        if (dispute.isEmpty() || !dispute.get().getReporter().getId().equals(loginUserPk)) {
            throw new ClientException(ILLEGAL_EX_MESSAGE);
        }
        if (dispute.get().getStatus() == DisputeStatus.STAND_BY) {
            dispute.get().setStatus(DisputeStatus.CANCELED);
            return new ResVo(Const.SUCCESS);
        }
        throw new ClientException(ILLEGAL_EX_MESSAGE);

    }

    @Transactional
    public MyBoardListVo getBoard(BoardDto dto) {
        Long loginUserPk = authenticationFacade.getLoginUserPk();
        Optional<User> user = userRepository.findById(loginUserPk);

        List<Board> boards = myPageBoardRepository.findByUser(user.get(), dto.getPage());
        return MyBoardListVo.builder().list(boards.stream().filter(prd -> prd.getStatus().equals(BoardStatus.ACTIVATED)).map(
                board -> MyBoardVo.builder()
                        .iboard(board.getId().intValue())
                        .title(board.getTitle())
                        .contents(board.getContents())
                        .istatus(board.getStatus().getNum())
                        .view(board.getView().intValue())
                        .CommentCount(myPageBoardComRepository.countBoardCommentByBoard(board))
                        .createdAt(board.getCreatedAt().toString())
                        .build()
        ).toList(
        )).build();
    }

    @Transactional
    public List<BuyPaymentSelVo> getReserveList(ReserveDto dto) {
        Long loginUserPk = authenticationFacade.getLoginUserPk();
        dto.setIuser(loginUserPk);

        if (dto.getRole() == 1) {
            List<BuyPaymentSelVo> list = mapper.getReservePayList(dto);
            return list;
        }
        if (dto.getRole() == 2) {
            List<BuyPaymentSelVo> list = mapper.getReserveProcList(dto);
            return list;
        }

        throw new ClientException(ILLEGAL_EX_MESSAGE);
    }

    public ProductListVo getProduct(ProductListDto dto) {
        Long loginUserPk = authenticationFacade.getLoginUserPk();
        Optional<User> user = userRepository.findById(dto.getTargetIuser());
        if (Objects.equals(loginUserPk, dto.getTargetIuser())) {
            List<Product> products = productRepository.findByUser(user.get(), dto.getPage());
            return ProductListVo.builder().vo(products.stream().filter(prd -> !prd.getStatus().equals(ProductStatus.DELETED)).map(
                    productss -> MyPageProductVo.builder().iproduct(productss.getId().longValue())
                            .tilte(productss.getTitle())
                            .price(productss.getRentalPrice())
                            .contents(productss.getContents())
                            .updatedAt(productss.getUpdatedAt().toLocalDate())
                            .build()
            ).toList()).build();
        }

        if (!Objects.equals(loginUserPk, dto.getTargetIuser())) {
            List<Product> products = productRepository.findByUser(user.get(), dto.getPage());

            return ProductListVo.builder().vo(products.stream().filter(prd -> prd.getStatus().equals(ProductStatus.ACTIVATED)).map(
                    productss -> MyPageProductVo.builder().iproduct(productss.getId().longValue())
                            .tilte(productss.getTitle())
                            .price(productss.getRentalPrice())
                            .contents(productss.getContents())
                            .updatedAt(productss.getUpdatedAt().toLocalDate())
                            .build()
            ).toList()).build();
        }
        return null;
    }

}
