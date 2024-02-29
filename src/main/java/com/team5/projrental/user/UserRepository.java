package com.team5.projrental.user;

import com.team5.projrental.entities.User;
import com.team5.projrental.entities.VerificationInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> , UserQueryRepository{

}
