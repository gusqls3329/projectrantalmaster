package com.team5.projrental.payment.thirdproj.refund;

import com.team5.projrental.entities.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund, Long>, RefundQueryRepository {
}
