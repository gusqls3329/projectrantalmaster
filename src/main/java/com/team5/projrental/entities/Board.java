package com.team5.projrental.entities;


import com.team5.projrental.entities.enums.BoardStatus;
import com.team5.projrental.entities.mappedsuper.BaseAt;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Board extends BaseAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iboard", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "iuser", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long view;

    @ToString.Exclude
    @OneToMany(mappedBy = "board", cascade = CascadeType.PERSIST)
    private List<BoardPic> boardPicList = new ArrayList();

    @Enumerated(EnumType.STRING)
    private BoardStatus status;
}
