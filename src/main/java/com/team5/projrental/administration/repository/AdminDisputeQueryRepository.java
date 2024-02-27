package com.team5.projrental.administration.repository;

import com.team5.projrental.entities.Dispute;
import com.team5.projrental.entities.enums.DisputeStatus;

import java.util.List;
import java.util.Optional;

public interface AdminDisputeQueryRepository {
    List<Dispute> findByLimitPage(int page, Integer div, String search, Integer category, Integer status);

    Optional<Dispute> findByIdAndStatus(Long idispute, DisputeStatus disputeStatus);

    Long totalCountByOptions(Integer div, String search, Integer category, Integer status);
}
