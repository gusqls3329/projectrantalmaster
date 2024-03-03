package com.team5.projrental.payment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team5.projrental.common.exception.ErrorMessage;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInsDto {
    @NotNull(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
    @Min(value = 1, message = ErrorMessage.ILLEGAL_RANGE_EX_MESSAGE)
    private Long iproduct;
//    @NotNull
//    @Min(0)
    @JsonIgnore
    private Long ibuyer;
    @JsonIgnore
//    @NotEmpty(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
    private String paymentMethod;
    @NotNull(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
    @FutureOrPresent(message = ErrorMessage.RENTAL_DATE_MUST_BE_AFTER_THAN_TODAY_EX_MESSAGE)
    private LocalDate rentalStartDate;
    @NotNull(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
    @FutureOrPresent(message = ErrorMessage.RENTAL_DATE_MUST_BE_AFTER_THAN_TODAY_EX_MESSAGE)
    private LocalDate rentalEndDate;
//    @NotNull(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
//    @Range(min = 50, max = 100, message = ErrorMessage.ILLEGAL_RANGE_EX_MESSAGE)
//    private Integer depositPer;

    @Min(value = 1, message = ErrorMessage.ILLEGAL_RANGE_EX_MESSAGE)
    private Long paymentDetailId;
    @Min(value = 100, message = ErrorMessage.ILLEGAL_RANGE_EX_MESSAGE)
    private Integer pricePerDate;
    @JsonIgnore
    private Integer totalPrice;

    //
    @JsonIgnore
    private Integer ipayment;
    @JsonIgnore
    private Integer rentalDuration;
    @JsonIgnore
    private Integer ipaymentMethod;
    @JsonIgnore
    private Integer price;
    @JsonIgnore
    private String code;
    @JsonIgnore
    private Integer deposit;


}
