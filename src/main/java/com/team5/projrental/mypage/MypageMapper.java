package com.team5.projrental.mypage;

import com.team5.projrental.mypage.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MypageMapper {

    List<BuyPaymentSelVo> getPaymentList(PaymentSelDto dto);
    List<BuyPaymentSelVo> getProductList(PaymentSelDto dto);
    List<MyBuyReviewListSelVo> getIbuyerReviewList(MyBuyReviewListSelDto dto);
    List<MyFavListSelVo> getFavList(MyFavListSelDto dto);

    List<AllReviewsForMeResultDto> getAllReviewFromMyProduct(Long iuser);


    List<BuyPaymentSelVo> getReservePayList(ReserveDto dto);
    List<BuyPaymentSelVo> getReserveProcList(ReserveDto dto);

}
