package com.team5.projrental.entities;

import com.team5.projrental.entities.embeddable.RentalDates;
import com.team5.projrental.entities.enums.PaymentMethod;
import com.team5.projrental.entities.inheritance.Users;
import com.team5.projrental.entities.mappedsuper.CreatedAt;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Payment extends CreatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ipayment")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "istock")
    private Stock stock;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "iproduct")
    private Product product;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "iuser")
    private User user;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private PaymentDetail paymentDetail;

    @Embedded
    private RentalDates rentalDates;

    private Integer duration;

    private Integer totalPrice;
    private Integer deposit;

    @Column(unique = true)
    private String code;
}
