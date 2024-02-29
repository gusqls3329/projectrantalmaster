package com.team5.projrental.payment.thirdproj;

import com.team5.projrental.entities.Payment;
import com.team5.projrental.entities.User;
import com.team5.projrental.payment.review.model.SelRatVo;

import java.time.LocalDate;

public interface PaymentQueryRepository {
    Payment selBuyRew(Long ipayment, Long iuser);
    User selUser(Long ipayment);
    SelRatVo selRat(Long iuser);

    boolean validateRentalDate(Long iproduct, LocalDate rentalStartDate, LocalDate rentalEndDate);
}
