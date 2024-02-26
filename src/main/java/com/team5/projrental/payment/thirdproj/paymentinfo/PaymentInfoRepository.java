package com.team5.projrental.payment.thirdproj.paymentinfo;

import com.team5.projrental.entities.Payment;
import com.team5.projrental.entities.PaymentInfo;
import com.team5.projrental.entities.User;

import com.team5.projrental.entities.enums.PaymentInfoStatus;
import com.team5.projrental.entities.ids.PaymentInfoIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, PaymentInfoIds> , PaymentInfoQueryRepository{
    PaymentInfo findByCode(String code);


    PaymentInfo findByPaymentAndUser(Payment payment, User user);

    @Query("""
        select pi from PaymentInfo pi
        join fetch pi.user
        join fetch pi.payment
        where pi.payment.id = :identity and
        pi.user != :user and
        pi.status in (:statuses)
    """)
    Optional<PaymentInfo> findByIdJoinFetch(Long identity, User user, List<PaymentInfoStatus> statuses);

}
