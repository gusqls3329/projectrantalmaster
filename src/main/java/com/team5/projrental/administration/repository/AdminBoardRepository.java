package com.team5.projrental.administration.repository;

import com.team5.projrental.entities.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminBoardRepository extends JpaRepository<Board, Long>, AdminBoardQueryRepository {
}
