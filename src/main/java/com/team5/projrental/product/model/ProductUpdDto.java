package com.team5.projrental.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team5.projrental.common.exception.ErrorMessage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdDto {

    @NotNull(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
    @Min(value = 1, message = ErrorMessage.ILLEGAL_RANGE_EX_MESSAGE)
    private Long iproduct;
    //    @NotNull
//    @Min(1)
    @JsonIgnore
    private Long iuser;
    private Categories icategory;
    @Length(min = 2, max = 100)
    private String addr;
    @Length(max = 30)
    private String restAddr;
    @Length(min = 2, max = 50)
    private String title;
    @Length(min = 2, max = 2000)
    private String contents;


    @Range(min = 100, max = Integer.MAX_VALUE, message = ErrorMessage.ILLEGAL_RANGE_EX_MESSAGE)
    private Integer rentalPrice;



    private LocalDate rentalStartDate;
    private LocalDate rentalEndDate;

    // add
    @Size(max = 9, message = ErrorMessage.ILLEGAL_RANGE_EX_MESSAGE)
    private List<Long> delPics;


    @JsonIgnore
    private Double x;
    @JsonIgnore
    private Double y;

    @Size(max = 10, message = ErrorMessage.ILLEGAL_RANGE_EX_MESSAGE)
    private List<String> hashTags;

    @Size(max = 10, message = ErrorMessage.ILLEGAL_RANGE_EX_MESSAGE)
    private List<Integer> delHashTags;

}

