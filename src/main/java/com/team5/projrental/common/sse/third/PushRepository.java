package com.team5.projrental.common.sse.third;

import com.team5.projrental.entities.Push;
import com.team5.projrental.entities.inheritance.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PushRepository extends JpaRepository<Push, Long> , PushQueryRepository{
    List<Push> findByUsers(Users users);



}
