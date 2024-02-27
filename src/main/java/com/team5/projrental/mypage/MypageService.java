package com.team5.projrental.mypage;

import com.team5.projrental.common.exception.thrid.ClientException;
import com.team5.projrental.common.security.AuthenticationFacade;
import com.team5.projrental.mypage.model.*;
import com.team5.projrental.payment.thirdproj.PaymentRepository;
import com.team5.projrental.product.thirdproj.japrepositories.product.ProductRepository;
import com.team5.projrental.user.UserRepository;
import com.team5.projrental.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.team5.projrental.common.exception.ErrorMessage.ILLEGAL_EX_MESSAGE;

@Service
@RequiredArgsConstructor
public class MypageService {
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final UsersRepository  usersRepository;

    private final MypageMapper mapper;
    private final AuthenticationFacade authenticationFacade;

    public List<BuyPaymentSelVo> getRentalList(PaymentSelDto dto){
        Long loginUserPk = authenticationFacade.getLoginUserPk();
        dto.setLoginedIuser(loginUserPk);
        if(dto.getRole() == 1) {
            List<BuyPaymentSelVo> list = mapper.getPaymentList(loginUserPk);
            return list;
        }
        if(dto.getRole() == 2){
            List<BuyPaymentSelVo> list = mapper.getProductList(loginUserPk);
            return list;
        }
        throw new ClientException(ILLEGAL_EX_MESSAGE);
    }

    public List<MyBuyReviewListSelVo> selIbuyerReviewList(MyBuyReviewListSelDto dto){
        Long loginUserPk = authenticationFacade.getLoginUserPk();
        dto.setIuser(loginUserPk);


        List<MyBuyReviewListSelVo> list = mapper.getIbuyerReviewList(dto);

        return list;
    }

    public List<MyFavListSelVo> getFavList(MyFavListSelDto dto) {
        Long loginUserPk = authenticationFacade.getLoginUserPk();
        dto.setLoginedIuser(loginUserPk);

        return mapper.getFavList(dto);
    }

    public List<MyBuyReviewListSelVo> getReview(MyBuyReviewListSelDto dto) {
        return null;
                //mapper.getAllReviewFromMyProduct(authenticationFacade.getLoginUserPk());
    }

    public List<MyDisputeVo> getDispute(MyBuyReviewListSelDto dto) {
        return null; //mapper.getDispute(dto);
    }

}
