package com.team5.projrental.mypage;

import com.team5.projrental.common.exception.ErrorMessage;
import com.team5.projrental.entities.Board;
import com.team5.projrental.entities.User;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyPageBoardRepository extends JpaRepository<Board, Long>, MyPageBoardQueryRepository{


}
