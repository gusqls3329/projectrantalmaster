package com.team5.projrental.mypage.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductListVo {
    private List<MyPageProductVo> vo;
}
