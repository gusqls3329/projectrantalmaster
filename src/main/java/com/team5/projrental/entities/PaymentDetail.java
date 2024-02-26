package com.team5.projrental.entities;

import com.team5.projrental.entities.enums.PaymentDetailCategory;
import com.team5.projrental.entities.mappedsuper.CreatedAt;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class PaymentDetail extends CreatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ipayment_detail")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "iuser")
    private User user;

    private String tid;

    @Enumerated(EnumType.STRING)
    private PaymentDetailCategory category;

    private Integer amount;

}
