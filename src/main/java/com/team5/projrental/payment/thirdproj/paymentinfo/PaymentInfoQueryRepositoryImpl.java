package com.team5.projrental.payment.thirdproj.paymentinfo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.entities.PaymentInfo;
import com.team5.projrental.entities.ids.PaymentInfoIds;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.team5.projrental.entities.QPaymentInfo.paymentInfo;

@Slf4j
@RequiredArgsConstructor
public class PaymentInfoQueryRepositoryImpl implements PaymentInfoQueryRepository {

    private final JPAQueryFactory query;


    public Optional<PaymentInfo> findByIdJoinFetchPaymentInfoAndProductBy(PaymentInfoIds ids) {
        return Optional.ofNullable(query.selectFrom(paymentInfo)
                .join(paymentInfo.payment).fetchJoin()
                .join(paymentInfo.payment.paymentDetail).fetchJoin()
                .join(paymentInfo.payment.product).fetchJoin()
                .where(paymentInfo.paymentInfoIds.eq(ids))
                .fetchOne());
    }

    @Override
    public Optional<PaymentInfo> findJoinFetchPaymentByCode(String code) {

        return Optional.ofNullable(query.selectFrom(paymentInfo)
                .join(paymentInfo.payment).fetchJoin()
                .where(paymentInfo.code.eq(code))
                .fetchOne());
    }

    @Override
    public Optional<PaymentInfo> findJoinFetchPaymentProductByUser(String code) {

        return Optional.ofNullable(query.selectFrom(paymentInfo)
                .join(paymentInfo.payment).fetchJoin()
                .join(paymentInfo.payment.product).fetchJoin()
                .join(paymentInfo.payment.paymentDetail).fetchJoin()
                .where(paymentInfo.code.eq(code))
                .orderBy(paymentInfo.createdAt.desc())
                .fetchOne());
    }


}
