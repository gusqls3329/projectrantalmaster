package com.team5.projrental.user.model;

import com.team5.projrental.entities.enums.Auth;
import lombok.Data;

@Data
public class UserEntity {
    private int iuser;
    private int iauth;
    private String uid;
    private String upw;
    private String nick;
    private String storedPic;
    private String firebaseToken;
    private Auth auth;
    private String createdAt;
    private String updatedAt;
}
