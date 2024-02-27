package com.team5.projrental.mypage.model;

import com.team5.projrental.product.model.Categories;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BuyPaymentSelVo {
    private int iproduct;
    private int ipayment;
    private String ProStoredPic;
    private int deposit;
    private int totalPrice;
    private LocalDate rentalEndDate;
    private LocalDate rentalStartDate;
    private int duration;
    private LocalDate createdAt;
    private String status;
    // 거래한 상대방 유저의
    private Long iuser;
    private String nick;
    private String userPic;

    private Categories icategory;

    public void setImainCategory(int imainCategory) {
        if (this.icategory == null) {
            this.icategory = new Categories();
        }
        this.icategory.setMainCategory(imainCategory);
    }

    public void setIsubCategory(int isubCategory) {
        if (this.icategory == null) {
            this.icategory = new Categories();
        }
        this.icategory.setSubCategory(isubCategory);
    }
}
