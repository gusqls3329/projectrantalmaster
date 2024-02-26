package com.team5.projrental.board;

import com.team5.projrental.board.model.BoardDelDto;
import com.team5.projrental.board.model.BoardSelVo;
import com.team5.projrental.board.model.BoardToggleLikeDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {
    BoardSelVo selBoard (int iboard);

    int delBoard(BoardDelDto dto);

    int delLike(BoardToggleLikeDto dto);

    int insLike(BoardToggleLikeDto dto);
}
