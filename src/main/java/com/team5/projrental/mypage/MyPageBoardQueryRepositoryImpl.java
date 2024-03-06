package com.team5.projrental.mypage;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.entities.Board;
import com.team5.projrental.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.team5.projrental.entities.QBoard.board;

@Slf4j
@RequiredArgsConstructor
public class MyPageBoardQueryRepositoryImpl implements MyPageBoardQueryRepository{
    private final JPAQueryFactory query;
    @Override
    public List<Board> findByUser(User user, int page) {

        return query.selectFrom(board)
                .join(board.user).fetchJoin()
                .where(board.user.id.eq(user.getId()))
                .offset(page)
                .limit(6)
                .orderBy(board.id.desc())
                .fetch();
    }
}
