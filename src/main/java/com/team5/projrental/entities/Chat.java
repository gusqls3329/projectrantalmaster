package com.team5.projrental.entities;

import com.team5.projrental.entities.mappedsuper.CreatedAt;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Chat extends CreatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ichat", columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "iproduct")
    private Product product;

    @Column(length = 2000, name = "last_msg")
    private String lastMsg;

    private String lastMsgAt;
}
