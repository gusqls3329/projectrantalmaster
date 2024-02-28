package com.team5.projrental.mypage;

import com.team5.projrental.entities.Board;
import com.team5.projrental.entities.User;

import java.util.List;

public interface MyPageBoardQueryRepository  {
    List<Board> findByUser(User user, int page);
}
