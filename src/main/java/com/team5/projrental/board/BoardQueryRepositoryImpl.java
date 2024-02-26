package com.team5.projrental.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.entities.Board;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.team5.projrental.entities.QBoard.board;

@RequiredArgsConstructor
public class BoardQueryRepositoryImpl implements BoardQueryRepository{

    private final JPAQueryFactory query;


}
