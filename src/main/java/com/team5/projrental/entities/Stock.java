package com.team5.projrental.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"iproduct", "seq"}))
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"iproduct"}))
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "istock")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "iproduct", unique = true)
    private Product product;

    private Long seq;
}
