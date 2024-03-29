package com.team5.projrental.entities;

import com.team5.projrental.entities.mappedsuper.CreatedAt;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"userName", "userPhone", "userBirthday"}))
public class VerificationInfo extends CreatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IVERIFICATION_INFO")
    private Long id;

    @Column(length = 1000)
    private String txId;

    private String userName;
    private String userPhone;
    private String userBirthday;
}
