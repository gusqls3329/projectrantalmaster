package com.team5.projrental.common.security;

import com.team5.projrental.common.security.model.SecurityPrincipal;
import com.team5.projrental.user.model.UserModel;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecurityUserDetails implements UserDetails, OAuth2User {

    private SecurityPrincipal securityPrincipal;
    private Map<String, Object> attributes;
    private UserModel userModel; //

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return securityPrincipal == null ? null :
                securityPrincipal.getRoles()
                        .stream()
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                        .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
