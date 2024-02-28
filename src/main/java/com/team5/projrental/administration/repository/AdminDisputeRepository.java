package com.team5.projrental.administration.repository;

import com.team5.projrental.entities.Dispute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminDisputeRepository extends JpaRepository<Dispute, Long>, AdminDisputeQueryRepository {

}
