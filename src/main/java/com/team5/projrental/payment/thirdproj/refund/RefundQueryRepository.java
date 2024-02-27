package com.team5.projrental.payment.thirdproj.refund;

import com.team5.projrental.entities.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefundQueryRepository {
    List<Refund> findAllLimitPage(Integer page, Integer status);

    Long totalCountByOptions(Integer status);
}
