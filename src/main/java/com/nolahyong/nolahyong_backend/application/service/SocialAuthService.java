package com.nolahyong.nolahyong_backend.application.service;

import com.nolahyong.nolahyong_backend.application.dto.SocialUserInfo;
import com.nolahyong.nolahyong_backend.application.service.social.SocialAuthProvider;
import com.nolahyong.nolahyong_backend.domain.model.User;
import com.nolahyong.nolahyong_backend.domain.model.enums.Provider;
import com.nolahyong.nolahyong_backend.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SocialAuthService {

    private final Map<String, SocialAuthProvider> authProviders;
    private final UserRepository userRepository;

    public User authenticate(String provider, String accessToken) {
        // 1. provider 이름으로 SocialAuthProvider 구현체 조회
        SocialAuthProvider authProvider = authProviders.get(provider.toLowerCase() + "AuthService");
        if (authProvider == null) {
            throw new IllegalArgumentException("Unsupported provider: " + provider);
        }

        // 2. 해당 provider의 authenticate 메서드로 SocialUserInfo 조회
        SocialUserInfo userInfo = authProvider.authenticate(accessToken);

        // 3. DB에서 사용자 찾거나 생성
        return findOrCreateUser(
                userInfo.getEmail(),
                userInfo.getNickname(),
                userInfo.getProviderId(),  // ★ 핵심 변경: socialId → providerId
                userInfo.getProvider()      // ★ provider 값도 함께 전달
        );
    }

    public User findOrCreateUser(String email, String nickname, String providerId, Provider provider) {
        return userRepository.findByProviderAndProviderId(provider, providerId)  // ★ 쿼리 메서드명 변경
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(email)
                                .nickname(nickname)
                                .provider(provider)
                                .providerId(providerId)  // ★ NOT NULL 필드 반드시 할당
                                .build()
                ));
    }
}