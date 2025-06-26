package com.nolahyong.nolahyong_backend.config;

import com.nolahyong.nolahyong_backend.domain.model.User;
import com.nolahyong.nolahyong_backend.domain.model.AccountStatus;
import com.nolahyong.nolahyong_backend.domain.model.SnsProvider;
import com.nolahyong.nolahyong_backend.application.port.out.LoadUserPort;
import com.nolahyong.nolahyong_backend.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final LoadUserPort loadUserPort;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (Exception ex) {
            OAuth2Error error = new OAuth2Error(
                    "oauth2_login_failed",
                    "OAuth2 로그인 처리 중 오류 발생: " + ex.getMessage(),
                    null
            );
            throw new OAuth2AuthenticationException(error, ex);
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String providerString = userRequest.getClientRegistration().getRegistrationId();
        SnsProvider provider = SnsProvider.valueOf(providerString.toUpperCase());

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String providerId = oAuth2User.getName();
        String email = (String) attributes.get("email");
        String nickname = (String) attributes.getOrDefault("name", attributes.get("nickname"));
        String profileImageUrl = (String) attributes.getOrDefault("picture", attributes.get("profile_image_url"));

        Optional<User> userOptional = loadUserPort.findByProviderAndProviderId(provider, providerId);

        User user = userOptional
                .map(existingUser -> updateExistingUser(existingUser, nickname, profileImageUrl))
                .orElseGet(() -> registerNewUser(provider, providerId, email, nickname, profileImageUrl));

        // CustomUserPrincipal로 래핑하여 반환
        return new CustomUserPrincipal(user, attributes);
    }

    private User registerNewUser(SnsProvider provider, String providerId, String email, String nickname, String profileImageUrl) {
        User user = User.builder()
                .provider(provider)
                .providerId(providerId)
                .email(email)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .accountStatus(AccountStatus.ACTIVE)
                .build();
        return loadUserPort.save(user);
    }

    private User updateExistingUser(User existingUser, String nickname, String profileImageUrl) {
        existingUser.setNickname(nickname);
        existingUser.setProfileImageUrl(profileImageUrl);
        return loadUserPort.save(existingUser);
    }
}