package com.team5.projrental.entities;


import com.team5.projrental.entities.enums.ReviewStatus;
import com.team5.projrental.entities.ids.PaymentInfoIds;
import com.team5.projrental.entities.inheritance.Users;
import com.team5.projrental.entities.mappedsuper.UpdatedAt;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                columnNames ={"ipayment", "iuser"}
        )
})
public class Review extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ireview")
    private Long id;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ipayment")
    private Payment payment;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "iuser")
    private User user;

    @Column(length = 2000)
    private String contents;

    private Integer rating;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;
}
