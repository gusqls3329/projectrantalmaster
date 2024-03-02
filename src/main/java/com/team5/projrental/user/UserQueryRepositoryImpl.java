package com.team5.projrental.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.entities.Dispute;
import com.team5.projrental.entities.User;
import com.team5.projrental.entities.VerificationInfo;
import com.team5.projrental.entities.enums.UserStatus;
import com.team5.projrental.mypage.MyPageDisputeQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.team5.projrental.entities.QDispute.dispute;
import static com.team5.projrental.entities.QUser.user;


@Slf4j
@RequiredArgsConstructor
public class UserQueryRepositoryImpl implements UserQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public User findByVerificationInfo(VerificationInfo info) {
        return jpaQueryFactory.selectFrom(user)
                .join(user.verificationInfo).fetchJoin()
                .where(user.verificationInfo.eq(info)
                        .and(user.status.eq(UserStatus.ACTIVATED)))
                .fetchOne();
    }

    @Override
    public User exFindByVerificationInfo(VerificationInfo info) {
        return jpaQueryFactory.selectFrom(user)
                .join(user.verificationInfo).fetchJoin()
                .where(user.verificationInfo.eq(info)
                        .and(user.status.eq(UserStatus.ACTIVATED)))
                .offset(0)
                .limit(1)
                .orderBy(user.penalty.asc())
                .fetchOne();
    }
}
