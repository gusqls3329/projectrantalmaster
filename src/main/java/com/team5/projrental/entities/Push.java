package com.team5.projrental.entities;

import com.team5.projrental.common.sse.enums.SseCode;
import com.team5.projrental.common.sse.enums.SseKind;
import com.team5.projrental.entities.inheritance.Users;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Push {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ipush")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "iusers")
    private Users users;

    private String description;
    @Enumerated(EnumType.STRING)
    private SseCode code;
    private Long identityNum;
    @Enumerated(EnumType.STRING)
    private SseKind kind;

}
