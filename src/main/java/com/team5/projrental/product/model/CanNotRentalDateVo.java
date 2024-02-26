package com.team5.projrental.product.model;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CanNotRentalDateVo {
    private LocalDate rentalStartDate;
    private LocalDate rentalEndDate;
}
