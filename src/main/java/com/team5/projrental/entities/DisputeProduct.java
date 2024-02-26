package com.team5.projrental.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class DisputeProduct extends Dispute{


    @BatchSize(size = 1000)
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "iproduct")
    private Product product;

}
