package com.team5.projrental.user;

import com.team5.projrental.entities.User;
import com.team5.projrental.entities.VerificationInfo;
import com.team5.projrental.user.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> , UserQueryRepository{

    Optional<User> findByUid(String id);
}
