package com.team5.projrental.user;

import com.team5.projrental.entities.User;
import com.team5.projrental.entities.VerificationInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


    User findByVerificationInfo(VerificationInfo info);



}
