package com.team5.projrental.mypage.model;

import com.team5.projrental.entities.enums.ProductMainCategory;
import com.team5.projrental.entities.enums.ProductSubCategory;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageProductVo {
    private ProductMainCategory mainCategory;
    private ProductSubCategory subCategory;
    private String storedPic;

    private Long iproduct;
    private String tilte;
    private String contents;
    private int price;
    private LocalDate updatedAt;
}
