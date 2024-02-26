package com.team5.projrental.administration.repository;

import com.team5.projrental.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepository extends JpaRepository<User, Long>, AdminUserQueryRepository {
}
