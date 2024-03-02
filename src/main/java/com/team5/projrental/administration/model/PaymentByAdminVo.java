package com.team5.projrental.administration.model;

import com.team5.projrental.entities.enums.PaymentDetailCategory;
import com.team5.projrental.entities.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
public class PaymentByAdminVo {

    private Long iproduct;
    private String title;
    private String prodMainPic;

    private Long ipayment;
    private LocalDate rentalStartDate;
    private LocalDate rentalEndDate;
    private Integer rentalDuration;
    private Integer totalPrice;
    private Integer deposit;
    private LocalDateTime createdAt;
    private Integer paymentStatus;

    private Long iuser;
    private String myPaymentCode;
    private String nick;
    private String phone;
    private String userStoredPic;
    private Double userRating;
    private UserStatus status;

    private PaymentDetailCategory method;

}
