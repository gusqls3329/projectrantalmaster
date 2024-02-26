package com.team5.projrental.payment.review.model;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SelRatVo {
    private Long countIre;
    private double rating;

    @QueryProjection
    public SelRatVo(Long countIre, double rating) {
        this.countIre = countIre;
        this.rating = rating;
    }
}
