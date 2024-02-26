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
public class ResolvedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //MySql에서 mariaDb에서 사용
    @Column(name = "iresolved_user")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "iadmin")
    private Admin admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iuser")
    private User user;

    @Enumerated(EnumType.STRING)
    private DisputeReason reason;
}
