package com.team5.projrental.user;

import com.team5.projrental.entities.User;
import com.team5.projrental.entities.inheritance.Users;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {

    Users findByUid(String uid);
}
