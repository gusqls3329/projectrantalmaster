package com.team5.projrental.payment.thirdproj;

import com.team5.projrental.entities.Payment;
import com.team5.projrental.entities.PaymentInfo;
import com.team5.projrental.entities.Review;
import com.team5.projrental.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> , PaymentQueryRepository{
    Payment findByCode(String genCode);


}
