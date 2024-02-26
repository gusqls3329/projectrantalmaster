package com.team5.projrental.payment.review;

import com.team5.projrental.entities.Payment;
import com.team5.projrental.entities.Review;
import com.team5.projrental.entities.User;
import com.team5.projrental.user.model.CheckIsBuyer;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Integer countByPaymentAndUser(Payment payment, User user);

    Review findByUser(User user);

}
