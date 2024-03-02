package com.team5.projrental.administration.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.common.Const;
import com.team5.projrental.entities.Board;
import com.team5.projrental.entities.enums.BoardStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import static com.team5.projrental.entities.QBoard.board;
import static com.team5.projrental.entities.QUser.user;

@Slf4j
@RequiredArgsConstructor
public class AdminBoardQueryRepositoryImpl implements AdminBoardQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Optional<Board> findActivatedById(Long iboard) {


        return Optional.ofNullable(
                query.selectFrom(board)
                        .join(board.user).fetchJoin()
                        .where(board.id.eq(iboard).and(board.status.eq(BoardStatus.ACTIVATED)))
                        .fetchOne()
        );
    }

    @Override
    public List<Board> findAllLimitPage(int page, Integer type, String search, Integer sort) {
        return query.selectFrom(board)
                .join(board.user).fetchJoin()
                .where(whereFindAllLimitPage(type, search))
                .offset(page)
                .limit(Const.ADMIN_PER_PAGE)
                .orderBy(orderByFindAllLimitPage(sort))
                .fetch();

    }

    @Override
    public Long totalCountByOptions(Integer type, String search) {
        return query.select(board.count())
                .from(board)
                .join(user).on(board.user.eq(user))
                .where(whereFindAllLimitPage(type, search))
                .fetchOne();

    }

    private OrderSpecifier<Long> orderByFindAllLimitPage(Integer sort) {
        return sort == null || sort == 0 ? board.id.desc() : board.view.desc();
    }

    private BooleanBuilder whereFindAllLimitPage(Integer type, String search) {
        BooleanBuilder builder = new BooleanBuilder();
        if (type == null) return builder;
        if (type == 1) {
            builder.and(board.user.nick.like("%" + search + "%"));
        }
        if (type == 2) {
            builder.and(board.title.like("%" + search + "%"));
        }
        return builder;
    }

}
