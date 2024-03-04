package com.team5.projrental.payment.review.model;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.team5.projrental.payment.review.model.QSelRatVo is a Querydsl Projection type for SelRatVo
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QSelRatVo extends ConstructorExpression<SelRatVo> {

    private static final long serialVersionUID = 1786427277L;

    public QSelRatVo(com.querydsl.core.types.Expression<Long> countIre, com.querydsl.core.types.Expression<Double> rating) {
        super(SelRatVo.class, new Class<?>[]{long.class, double.class}, countIre, rating);
    }

}

