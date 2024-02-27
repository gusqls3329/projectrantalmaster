package com.team5.projrental.board;

import com.team5.projrental.board.model.*;
import com.team5.projrental.entities.BoardPic;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {

    int insBoard(BoardInsDto dto);

    int insBoardPics(BoardPicInsDto dto);
    BoardSelVo selBoard (int iboard);

    int viewCount(int iboard); //조회수 증가
    int putboard(BoardPutDto dto);

    int delBoard(int iboard);

    int delLike(BoardToggleLikeDto dto);

    int insLike(BoardToggleLikeDto dto);
}
