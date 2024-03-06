package com.team5.projrental.mypage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team5.projrental.common.exception.ErrorMessage;
import com.team5.projrental.entities.enums.ProductMainCategory;
import com.team5.projrental.entities.enums.ProductSubCategory;
import com.team5.projrental.product.model.Categories;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class MyBuyReviewListSelVo {

    private int mainCategory;
    private int subCategory;


    private int ireview; // 리뷰 번호(PK)
    private int raiting; // 별 점수
    private String contents; // 로그인 유저가 남긴 후기
    private String nick; // 로그인 유저 닉네임

    private String loginedUserPic; // 로그인 유저프로필사진
    private String title; // 상품 제목
    @Schema(title = "상품 대표사진")
    private String prodPic; // 상품 대표사진
    private int iproduct; // 제품 PK

    @JsonIgnore
    @Range(min = 1, message = ErrorMessage.ILLEGAL_RANGE_EX_MESSAGE)
    private int page;
    @JsonIgnore
    private int startIdx;
    @JsonIgnore
    private int rowCount = 6;//

    public void setPage(int page) {
        this.startIdx = (page-1) * this.rowCount;
    }

    public void setMainCategoryy(String mainCategoryy) {
//        this.mainCategoryy = mainCategoryy;
        mainCategory = ProductMainCategory.valueOf(mainCategoryy).getNum();
    }
    public void setSubCategoryy(String subCategoryy) {
//        this.subCategoryy = subCategoryy;
        subCategory = ProductSubCategory.valueOf(subCategoryy).getNum();
    }
}
