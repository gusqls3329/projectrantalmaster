package com.team5.projrental.mypage.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageProductVo {
    private Long iproduct;
    private String tilte;
    private String contents;
    private int price;
    private LocalDate updatedAt;
}
