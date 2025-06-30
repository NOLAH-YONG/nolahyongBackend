package com.nolahyong.nolahyong_backend.application.service.social;

import com.nolahyong.nolahyong_backend.application.dto.SocialUserInfo;
import com.nolahyong.nolahyong_backend.domain.model.enums.Provider;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleAuthService implements SocialAuthProvider {

    private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public SocialUserInfo authenticate(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                USER_INFO_URL,
                HttpMethod.GET,
                entity,
                String.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("구글 사용자 정보 조회 실패: " + response.getStatusCode());
        }

        try {
            JSONObject body = new JSONObject(response.getBody());
            String socialId = body.getString("sub"); // 구글의 고유 ID는 "sub"
            String email = body.optString("email", null);
            String nickname = body.optString("name", null);

            return SocialUserInfo.builder()
                    .email(email)
                    .nickname(nickname)
                    .providerId(socialId)
                    .provider(Provider.GOOGLE)
                    .build();
        } catch (org.json.JSONException e) {
            throw new RuntimeException("구글 사용자 정보 JSON 파싱 실패: " + e.getMessage(), e);
        }
    }
}