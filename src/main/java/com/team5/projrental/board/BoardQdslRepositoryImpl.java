package com.team5.projrental.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class BoardQdslRepositoryImpl implements BoardQdslRepository{
    private final JPAQueryFactory jpaQueryFactory;

    /*@Override
    public List<BoardListSelVo> selboardAll(BoardListSelDto dto, Pageable pageable) {
        JPAQuery<Board> jpaQuery = jpaQueryFactory.select(board)
                .from(board)
                .join(board.users).fetchJoin();

        return null;
    }*/

    /*@Override
    public List<BoardListSelVo> selboardAll(User user, Pageable pageable) {
        List<Board> boardList = jpaQueryFactory.select(board)
                .from(board)
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<BoardListSelVo> list = boardList.stream().map(item ->
                BoardListSelVo.builder()
                        .iboard(item.getId().intValue())
                        //.totalBoardCount()
                        .title(item.getTitle())
                        .view(item.getView().intValue())
                        //.createdAt(item.g)
                        .nick(user.getNick())
                        //.isLiked()
                        .build())
                .collect(Collectors.toList());



        return null;
    }*/

}


