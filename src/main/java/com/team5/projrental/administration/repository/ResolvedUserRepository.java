package com.team5.projrental.administration.repository;

import com.team5.projrental.entities.ResolvedUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResolvedUserRepository extends JpaRepository<ResolvedUser, Long> {
}
