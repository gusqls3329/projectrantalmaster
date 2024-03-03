package com.team5.projrental.common.security.model;

import com.team5.projrental.entities.enums.Auth;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SecurityPrincipal {
    private Long iuser;
    private String auth;

    @Builder.Default
    private List<String> roles = new ArrayList<>();
}
