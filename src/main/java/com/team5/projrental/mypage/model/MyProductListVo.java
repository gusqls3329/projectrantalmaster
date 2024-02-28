package com.team5.projrental.mypage.model;

import lombok.*;

import java.util.List;

@Builder
@Getter
public class MyProductListVo {
    private List<MyProductVo> myProductVo;
}
