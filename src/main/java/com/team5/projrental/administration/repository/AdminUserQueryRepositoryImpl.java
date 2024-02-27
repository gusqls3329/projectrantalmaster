package com.team5.projrental.administration.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.common.Const;
import com.team5.projrental.entities.User;
import com.team5.projrental.entities.enums.UserStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.team5.projrental.entities.QUser.user;

@Slf4j
@RequiredArgsConstructor
public class AdminUserQueryRepositoryImpl implements AdminUserQueryRepository {

    private final JPAQueryFactory query;
    private final EntityManager em;

    @Override
    public List<User> findUserByOptions(Integer page, Integer searchType, String search, UserStatus status) {

        return query.selectFrom(user)
                .where(whereFindByUserByOptions(searchType, search, status))
                .offset(page)
                .limit(Const.ADMIN_PER_PAGE)
                .orderBy(user.id.desc())
                .fetch();


    }

    @Override
    public Long totalCountByOptions(Integer searchType, String search, UserStatus status) {
        return query.select(user.count())
                .from(user)
                .where(whereFindByUserByOptions(searchType, search, status))
                .orderBy(user.id.desc())
                .fetchOne();

    }

    public BooleanBuilder whereFindByUserByOptions(Integer searchType, String search, UserStatus status) {
        if (searchType == null && status == null) return null;

        BooleanBuilder builder = new BooleanBuilder();

        if (searchType != null) {
            if (searchType == 1 || searchType == 2) {
                builder.and(user.nick.like("%" + search + "%"));
            }
            if (searchType == 1 || searchType == 3) {
                builder.and(user.uid.like("%" + search + "%"));
            }
        }
        if (status == null) {
            return builder;
        }

        builder.and(user.status.eq(status));

        return builder;


    }

}
