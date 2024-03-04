package com.team5.projrental.mypage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team5.projrental.entities.enums.ProductMainCategory;
import com.team5.projrental.entities.enums.ProductSubCategory;
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
    private String title;
    private int mainCategory;
    private int subCategory;
//    @JsonIgnore
//    private String mainCategoryy;
//    @JsonIgnore
//    private String subCategoryy;
    // 거래한 상대방 유저의
    private Long iuser;
    private String nick;
    private String userPic;

    public void setMainCategoryy(String mainCategoryy) {
//        this.mainCategoryy = mainCategoryy;
        mainCategory = ProductMainCategory.valueOf(mainCategoryy).getNum();
    }
    public void setSubCategoryy(String subCategoryy) {
//        this.subCategoryy = subCategoryy;
        subCategory = ProductSubCategory.valueOf(subCategoryy).getNum();
    }
}
