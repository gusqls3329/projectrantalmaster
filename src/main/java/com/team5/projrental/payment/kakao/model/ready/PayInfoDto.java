package com.team5.projrental.payment.kakao.model.ready;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team5.projrental.common.exception.ErrorMessage;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@Getter
@Setter
public class PayInfoDto {
    @JsonIgnore
    private Integer totalPrice;
    @JsonIgnore
    private Integer deposit;
    @NotBlank(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
    @Length(min = 1, max = 100, message = ErrorMessage.ILLEGAL_RANGE_EX_MESSAGE)
    private String itemName;

    @NotNull(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
    @Range(min = 100, max = 1000000, message = "가격은 100원 이상 1,000,000원 이하로 입력해주세요.")
    private Integer pricePerDay;
    @NotNull(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
    @FutureOrPresent(message = ErrorMessage.RENTAL_DATE_MUST_BE_AFTER_THAN_TODAY_EX_MESSAGE)
    private LocalDate rentalStartDate;
    @NotNull(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
    @FutureOrPresent(message = ErrorMessage.RENTAL_END_DATE_MUST_BE_AFTER_THAN_RENTAL_START_DATE_EX_MESSAGE)
    private LocalDate rentalEndDate;


}
