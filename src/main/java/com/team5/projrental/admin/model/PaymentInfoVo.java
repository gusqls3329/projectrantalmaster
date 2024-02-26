package com.team5.projrental.admin.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PaymentInfoVo {
    private String method;
    private Integer totalPrice;
    private Integer deposit;
    private LocalDate rentalStartDate;
    private LocalDate rentalEndDate;
    private String code;
    private String myPaymentCode;
    private Long ipayment;
    private String title;
    private String prodMainPic;
    private String paymentStatus;
}
