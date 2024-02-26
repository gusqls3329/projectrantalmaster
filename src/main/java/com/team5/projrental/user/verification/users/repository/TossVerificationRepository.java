package com.team5.projrental.user.verification.users.repository;

import com.team5.projrental.entities.VerificationInfo;
import com.team5.projrental.user.verification.users.model.VerificationUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TossVerificationRepository extends JpaRepository<VerificationInfo, Long> {

    Optional<VerificationInfo> findByUserNameAndUserPhoneAndUserBirthday(String userName, String userPhone, String userBirthday);

    @Query("select v.id from VerificationInfo v where v.userName = :userName and v.userBirthday = :userBirthday and v.userPhone" +
           " = :userPhone")
    Long findByUserInfos(String userName, String userBirthday, String userPhone);
}
