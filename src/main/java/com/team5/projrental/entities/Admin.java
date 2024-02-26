package com.team5.projrental.entities;

import com.team5.projrental.entities.enums.AdminStatus;
import com.team5.projrental.entities.inheritance.Users;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Admin extends Users {


    private String nick;

    private String storedAdminPic;

    @Enumerated(EnumType.STRING)
    private AdminStatus status;

}
