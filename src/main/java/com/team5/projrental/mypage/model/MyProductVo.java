package com.team5.projrental.mypage.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MyProductVo {
    private Long iproduct;
    private String tilte;
    private String contents;
    private String price;
    private LocalDate updatedAt;
}
