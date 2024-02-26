package com.team5.projrental.entities;

import com.team5.projrental.entities.enums.ProvideType;
import com.team5.projrental.entities.enums.UserStatus;
import com.team5.projrental.entities.inheritance.Users;
import com.team5.projrental.entities.mappedsuper.BaseUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends Users {

    @Embedded
    private BaseUser baseUser;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "iverification_info")
    private VerificationInfo verificationInfo;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private String nick;

    @Enumerated(EnumType.STRING)
    private ProvideType provideType;

    @ColumnDefault("0")
    private byte penalty;


}
