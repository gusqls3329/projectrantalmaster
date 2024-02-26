package com.team5.projrental.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DisputePayment extends Dispute{




    @BatchSize(size = 1000)
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "ipayment"),
            @JoinColumn(name = "iuser")
    })
    private PaymentInfo paymentInfo;

}
