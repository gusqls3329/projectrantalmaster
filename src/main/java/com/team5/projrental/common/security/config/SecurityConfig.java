package com.team5.projrental.common.security.config;

import com.team5.projrental.common.security.ex.JwtAccessDeniedHandler;
import com.team5.projrental.common.security.ex.JwtAuthenticationEntryPoint;
import com.team5.projrental.common.security.filter.JwtAuthenticationFilter;
import com.team5.projrental.common.security.oauth2.CustomeOAuth2UserService;
import com.team5.projrental.common.security.oauth2.OAth2AuthenticationSuccessHandler;
import com.team5.projrental.common.security.oauth2.OAuth2AuthenticationRequestBasedOnCookieRepository;
import com.team5.projrental.common.security.oauth2.Oauth2AuthenticationFailureHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final Oauth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;
    private final OAuth2AuthenticationRequestBasedOnCookieRepository oAuth2AuthenticationRequestBasedOnCookieRepository;
    private final OAth2AuthenticationSuccessHandler oAth2AuthenticationSuccessHandler;
    private final CustomeOAuth2UserService customeOAuth2UserService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.sessionManagement(authz -> authz.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth.requestMatchers(
                                "/api/mypage/**",
                                "/api/chat/**",
                                "/api/pay/review",
                                "/api/user/signout",
                                "/api/user/fcm",
                                "/api/user/refrech-token",
                                "/api/user/firebase-token",
                                "/api/pay/**",

                                // 3차
                                "/api/pay/kakao/**",
                                "/api/dispute/**",
                                "/api/sse/connect",
                                "/api/bot/**"
                        ).authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/user").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/user").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/user").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/prod").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/prod").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/prod").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/prod").authenticated()
                        // 권한
                        .requestMatchers("/api/admin/**").hasAnyRole("ADMIN")
                        .requestMatchers("/api/sse/connect").hasAnyRole("USER")
                        .requestMatchers(
                                "/api/prod/fav/**",
                                "/api/pay/kakao/**",
                                "/api/board/like/"
                        ).hasAnyRole("USER")
                        .requestMatchers(
                                HttpMethod.POST, "/api/prod",
                                "/api/board"
                        ).hasRole("USER")
                        //
                        .anyRequest().permitAll())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(except -> {
                    except.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                            .accessDeniedHandler(new JwtAccessDeniedHandler());
                })
                .oauth2Login(oauth2 -> oauth2.authorizationEndpoint(
                                        auth -> auth.baseUri("/oauth2/authorization")
                                                .authorizationRequestRepository(oAuth2AuthenticationRequestBasedOnCookieRepository)

                                ).redirectionEndpoint(redirection -> redirection.baseUri("/*/oauth2/code/*"))
                                .userInfoEndpoint(userInfo -> userInfo.userService(customeOAuth2UserService))
                                .successHandler(oAth2AuthenticationSuccessHandler)
                                .failureHandler(oauth2AuthenticationFailureHandler)
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
