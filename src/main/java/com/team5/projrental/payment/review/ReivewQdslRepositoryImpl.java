package com.team5.projrental.payment.review;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.entities.Review;
import com.team5.projrental.user.model.CheckIsBuyer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.team5.projrental.entities.QPayment.payment;
import static com.team5.projrental.entities.QReview.review;

@Slf4j
@RequiredArgsConstructor
public class ReivewQdslRepositoryImpl implements ReivewQdslRepository{
    private final JPAQueryFactory jpaQueryFactory;


}
