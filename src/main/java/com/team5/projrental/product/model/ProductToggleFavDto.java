package com.team5.projrental.product.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductToggleFavDto {
    private Long iuser;
    private int iproduct;

    @JsonIgnore
    LocalDateTime createdAt = LocalDateTime.now();
}
