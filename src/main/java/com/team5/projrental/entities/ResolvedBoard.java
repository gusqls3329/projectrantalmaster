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
public class ResolvedBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iresolved_board")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "iadmin")
    private Admin admin;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iboard")
    private Board board;

    private DisputeReason reason;
}
