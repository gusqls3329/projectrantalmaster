package com.team5.projrental.entities;

import com.team5.projrental.entities.enums.DisputeReason;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class ResolvedProduct{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iresolved_product")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "iadmin")
    private Admin admin;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iproduct")
    private Product product;

    private DisputeReason reason;

}
