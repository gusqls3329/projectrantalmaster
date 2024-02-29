package com.team5.projrental.aachat.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatProductInfo {
    private Long iproduct;
    private String title;
    private String prodPic; // 제품 대표사진
}
