package com.team5.projrental.administration.model;

import com.team5.projrental.administration.model.inner.ProductInfoByAdmin;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProductByAdminVo {
    private Long totalDisputeCount;
    private List<ProductInfoByAdmin> products;
}
