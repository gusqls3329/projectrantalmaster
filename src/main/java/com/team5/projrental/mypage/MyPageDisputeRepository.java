package com.team5.projrental.mypage;

import com.team5.projrental.entities.Dispute;
import com.team5.projrental.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyPageDisputeRepository extends JpaRepository<Dispute, Long> ,MyPageDisputeQueryRepository{

}
