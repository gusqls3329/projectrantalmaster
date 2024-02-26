package com.team5.projrental.entities;

import com.team5.projrental.entities.inheritance.Users;
import com.team5.projrental.entities.mappedsuper.BaseAt;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class BoardComment extends BaseAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iboard_comment",nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "iboard", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Board board;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "iuser", nullable = false)
    private User user;

    @Column(nullable = false)
    private String comment;

}
