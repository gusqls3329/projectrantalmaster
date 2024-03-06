package com.team5.projrental.common.security.oauth2;


import com.team5.projrental.common.security.SecurityUserDetails;
import com.team5.projrental.common.security.model.SecurityPrincipal;
import com.team5.projrental.common.security.oauth2.userinfo.Oauth2UserInfo;
import com.team5.projrental.common.security.oauth2.userinfo.Oauth2UserInfoFactory;
import com.team5.projrental.entities.User;
import com.team5.projrental.user.UserMapper;
import com.team5.projrental.user.UserRepository;
import com.team5.projrental.user.model.UserModel;
import com.team5.projrental.user.model.UserSelDto;
import com.team5.projrental.user.model.UserSignupProcDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomeOAuth2UserService extends DefaultOAuth2UserService {
    private final UserMapper mapper;
    private final Oauth2UserInfoFactory factory;
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        SocialProviderType socialProviderType = SocialProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        Oauth2UserInfo oauth2UserInfo = factory.getOauth2UserInfo(socialProviderType, user.getAttributes());
        UserSelDto dto = UserSelDto.builder()
                .providerType(socialProviderType.name())
                .uid(oauth2UserInfo.getId()).build();
        User userss = userRepository.findByUid(dto.getUid()).orElse(null);
        UserModel userModel = null;

        if (userss != null) {
            userModel = UserModel.builder()
                    .iuser(userss.getId())
                    .auth(userss.getAuth())
                    .address(userss.getBaseUser().getAddress())
                    .phone(userss.getPhone())
                    .id(userss.getId())
                    .email(userss.getEmail())
                    .nick(userss.getNick())
                    .provideType(userss.getProvideType().name())
                    .rating(userss.getBaseUser().getRating())
                    .penalty(userss.getPenalty())
                    .storedPic(userss.getBaseUser().getStoredPic())
                    .verification(userss.getBaseUser().getVerification())
                    .status(userss.getStatus())
                    .build();

            SecurityPrincipal myPrincipal = SecurityPrincipal.builder()
                    .iuser(userModel.getId())
                    .build();
            myPrincipal.getRoles().add(userss.getAuth().name());

            return SecurityUserDetails.builder()
                    .userModel(userModel)
                    .securityPrincipal(myPrincipal)
                    .attributes(user.getAttributes()).build();
        }

        userModel = signupUser(oauth2UserInfo, socialProviderType);
        return SecurityUserDetails.builder()
                .userModel(userModel)
                .attributes(user.getAttributes()).build();

    }

    private UserModel signupUser(Oauth2UserInfo oauth2UserInfo, SocialProviderType socialProviderType) {

        return UserModel.builder()
                .uid(oauth2UserInfo.getId())
                .upw("social")
                .provideType(SocialProviderType.KAKAO.name())
                .build();
    }

}