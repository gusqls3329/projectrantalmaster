package com.team5.projrental.entities.ids;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class PaymentInfoIds implements Serializable {

    private Long ipayment;
    private Long iuser;



}
