package com.team5.projrental.dispute.model;

import com.team5.projrental.common.exception.ErrorMessage;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class DisputeDto {

    @NotNull(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
    @Range(min = -2, max = 6, message = ErrorMessage.ILLEGAL_RANGE_EX_MESSAGE)
    private Integer reason;
    @Range(min = 1, max = 5, message = ErrorMessage.ILLEGAL_RANGE_EX_MESSAGE)
    @NotNull(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
    private Long identity;
    @NotNull(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
    private String details;

}
