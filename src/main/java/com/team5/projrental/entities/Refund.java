package com.team5.projrental.entities;

import com.team5.projrental.entities.enums.RefundStatus;
import com.team5.projrental.entities.mappedsuper.BaseAt;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Refund extends BaseAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "irefund")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "iuser")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "iadmin")
    private Admin admin;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ipayment")
    private Payment payment;

    private Integer refundAmount;

    private RefundStatus status;
}
