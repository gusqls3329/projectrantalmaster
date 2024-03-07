package com.team5.projrental.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team5.projrental.common.exception.ErrorMessage;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInsDto {
    //    @NotNull
    @JsonIgnore
    private Long iuser;
    @NotBlank(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
    @Length(min = 2, max = 50)
    private String title;
    @NotBlank(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
    @Length(min = 2, max = 2000)
    private String contents;
    @NotBlank(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
    @Length(min = 2, max = 100)
    private String addr;
    @NotBlank(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
    @Length(max = 30)
    private String restAddr;
    @JsonIgnore
    private String mainPic;
    @JsonIgnore
    private List<String> pics;
    @JsonIgnore
//    @NotNull(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
    @Range(min = 100, max = Integer.MAX_VALUE, message = ErrorMessage.ILLEGAL_RANGE_EX_MESSAGE)
    private Integer price;
    @NotNull(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
    @Range(min = 100, max = Integer.MAX_VALUE, message = ErrorMessage.ILLEGAL_RANGE_EX_MESSAGE)
    private Integer rentalPrice;


    @NotNull(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
    @FutureOrPresent(message = ErrorMessage.ILLEGAL_DATE_EX_MESSAGE)
    private LocalDate rentalStartDate;
    @NotNull(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
    @FutureOrPresent(message = ErrorMessage.ILLEGAL_DATE_EX_MESSAGE)
    private LocalDate rentalEndDate;
    @NotNull(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
    private Categories icategory;

    @Size(max = 10, message = ErrorMessage.ILLEGAL_RANGE_EX_MESSAGE)
    private List<String> hashTags;
}
