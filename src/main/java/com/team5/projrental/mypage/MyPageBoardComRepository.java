package com.team5.projrental.mypage;

import com.team5.projrental.entities.Board;
import com.team5.projrental.entities.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyPageBoardComRepository  extends JpaRepository<BoardComment, Long> {
    BoardComment findByBoard(Board board);
}
