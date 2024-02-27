package com.team5.projrental.administration.repository;

import com.team5.projrental.entities.Board;

import java.util.List;
import java.util.Optional;

public interface AdminBoardQueryRepository {
    Optional<Board> findActivatedById(Long iboard);

    List<Board> findAllLimitPage(int page, Integer type, String search, Integer sort);

    Long totalCountByOptions(Integer type, String search);
}
