package com.team5.projrental.payment.thirdproj.refund;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.common.Const;
import com.team5.projrental.entities.Refund;
import com.team5.projrental.entities.enums.RefundStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.team5.projrental.entities.QRefund.refund;

@Slf4j
@RequiredArgsConstructor
public class RefundQueryRepositoryImpl implements RefundQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<Refund> findAllLimitPage(Integer page, Integer status) {
        return query.selectFrom(refund)
                .where(whereFindAllLimitPage(status))
                .offset(page)
                .limit(Const.ADMIN_PER_PAGE)
                .orderBy(refund.id.desc())
                .fetch();


    }

    @Override
    public Long totalCountByOptions(Integer status) {
        return query.select(refund.count())
                .from(refund)
                .where(whereFindAllLimitPage(status))
                .fetchOne();
    }

    private BooleanExpression whereFindAllLimitPage(Integer status) {
        return status == null ? null : refund.status.eq(RefundStatus.getByNum(status))
                .and(refund.refundAmount.gt(0));
    }

}
