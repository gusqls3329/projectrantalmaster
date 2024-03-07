package com.team5.projrental.user.verification.users.model;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VerificationRedisForm {

    private String id;
    private String txId;

    private String userName;
    private String userPhone;
    private String userBirthday;

}
