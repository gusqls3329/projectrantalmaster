package com.team5.projrental.user.model;

import com.team5.projrental.entities.VerificationInfo;
import com.team5.projrental.entities.embeddable.Address;
import com.team5.projrental.entities.enums.Auth;
import com.team5.projrental.entities.enums.ProvideType;
import com.team5.projrental.entities.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

@Data
@Builder
public class UserModel {
    private Long id;
    private Long iuser;
    private Auth auth;
    private String uid;
    private String upw;
    private String phone;
    private String email;
    private UserStatus status;
    private String nick;
    private String provideType;
    private byte penalty;
    private Address address;
    private String storedPic;
    private Double rating;
    private String verification;

}