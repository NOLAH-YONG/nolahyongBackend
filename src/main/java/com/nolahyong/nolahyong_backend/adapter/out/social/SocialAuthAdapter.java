package com.nolahyong.nolahyong_backend.adapter.out.social;

import com.nolahyong.nolahyong_backend.application.dto.SocialUserInfo;
import com.nolahyong.nolahyong_backend.application.port.out.SocialAuthPort;
import com.nolahyong.nolahyong_backend.application.port.out.SocialAuthUserInfoPort;
import com.nolahyong.nolahyong_backend.domain.model.User;
import com.nolahyong.nolahyong_backend.domain.model.enums.Provider;
import com.nolahyong.nolahyong_backend.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SocialAuthAdapter implements SocialAuthPort {

    private final Map<String, SocialAuthUserInfoPort> authProviders;
    private final UserRepository userRepository;

    @Override
    public User authenticate(String provider, String accessToken) {
        // provider 이름을 소문자/대문자 통일, 빈 이름과 일치시킴
        String beanName = provider.toLowerCase() + "SocialAuthAdapter";
        SocialAuthUserInfoPort authProvider = authProviders.get(beanName);
        if (authProvider == null) {
            throw new IllegalArgumentException("Unsupported provider: " + provider);
        }

        SocialUserInfo userInfo = authProvider.authenticate(accessToken);

        return findOrCreateUser(
                userInfo.getEmail(),
                userInfo.getNickname(),
                userInfo.getProviderId(),
                userInfo.getProvider()
        );
    }

    public User findOrCreateUser(String email, String nickname, String providerId, Provider provider) {
        return userRepository.findByProviderAndProviderId(provider, providerId)  // ★ 쿼리 메서드명 변경
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(email)
                                .nickname(nickname)
                                .provider(provider)
                                .providerId(providerId)
                                .build()
                ));
    }
}