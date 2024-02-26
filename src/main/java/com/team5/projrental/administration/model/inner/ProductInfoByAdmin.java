package com.team5.projrental.administration.model.inner;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProductInfoByAdmin {
    private Long iproduct;
    private Long iuser;
    private Integer mainCategory;
    private Integer subCategory;
    private Integer pricePerDay;
    private Long view;
    private LocalDateTime createdAt;
    private Integer status;

}
