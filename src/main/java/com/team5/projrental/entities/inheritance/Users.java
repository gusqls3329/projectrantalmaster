package com.team5.projrental.entities.inheritance;

import com.team5.projrental.entities.enums.Auth;
import com.team5.projrental.entities.mappedsuper.BaseAt;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@DiscriminatorColumn
public class Users extends BaseAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iuser", columnDefinition = "BIGINT UNSIGNED", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Auth auth;

    @Column(unique = true, length = 20, nullable = false)
    private String uid;
    @Column(length = 2100, nullable = false)
    private String upw;
    @Column(length = 13, nullable = false)
    private String phone;
    @Column(length = 30, nullable = false)
    private String email;
}
