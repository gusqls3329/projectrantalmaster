package com.team5.projrental.entities;

import com.team5.projrental.entities.mappedsuper.BaseAt;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class ChatBot extends BaseAt {
    @Id
    @Column(name = "ichat_bot", columnDefinition = "BIGINT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer grp;
    private Integer depth;
    private Integer level;
    private String mention;
    private String redirectUrl;

}
