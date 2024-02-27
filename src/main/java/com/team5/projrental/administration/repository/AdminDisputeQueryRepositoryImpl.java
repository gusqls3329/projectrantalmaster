package com.team5.projrental.administration.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.common.Const;
import com.team5.projrental.entities.Dispute;
import com.team5.projrental.entities.enums.DisputeReason;
import com.team5.projrental.entities.enums.DisputeStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import static com.team5.projrental.entities.QDispute.dispute;

@Slf4j
@RequiredArgsConstructor
public class AdminDisputeQueryRepositoryImpl implements AdminDisputeQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<Dispute> findByLimitPage(int page, Integer div, String search, Integer category, Integer status) {

        return query.selectFrom(dispute)
                .where(whereFindByLimitPage(div, search, category, status))
//                .offset(page)
//                .limit(Const.ADMIN_PER_PAGE)
                .orderBy(dispute.id.desc())
                .fetch();

    }

    @Override
    public Optional<Dispute> findByIdAndStatus(Long idispute, DisputeStatus disputeStatus) {
        return Optional.ofNullable(query.selectFrom(dispute)
                .join(dispute.reportedUser).fetchJoin()
                .join(dispute.reporter).fetchJoin()
                .where(dispute.id.eq(idispute).and(dispute.status.eq(disputeStatus)))
                .fetchOne());
    }

    private BooleanBuilder whereFindByLimitPage(Integer div, String search, Integer category, Integer status) {
        BooleanBuilder builder = new BooleanBuilder();
        if (div != null) {
            List<DisputeReason> reasons = DisputeReason.getByDiv(div);
            builder.and(dispute.reason.in(reasons));
        }
        if (search != null) {
            builder.and(dispute.reporter.uid.like("%" + search + "%")
                    .or(dispute.reportedUser.uid.like("%" + search + "%")));
        }
        if (category != null) {
            builder.and(dispute.reason.eq(DisputeReason.getByNum(category)));
        }
        if (status != null) {
            builder.and(dispute.status.eq(DisputeStatus.getByNum(status)));
        }
        return builder;

    }
}
