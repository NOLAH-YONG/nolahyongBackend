// application/service/social
package com.nolahyong.nolahyong_backend.application.service.social;

import com.nolahyong.nolahyong_backend.application.dto.SocialUserInfo;
import com.nolahyong.nolahyong_backend.domain.model.User;
import com.nolahyong.nolahyong_backend.domain.model.enums.Provider;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoAuthService implements SocialAuthProvider {

    private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    @Override
    public SocialUserInfo authenticate(String accessToken) {
        // 1. 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 2. API 호출
        ResponseEntity<String> response = new RestTemplate().exchange(
                USER_INFO_URL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        try {
            JSONObject body = new JSONObject(response.getBody());
            String socialId = String.valueOf(body.get("id"));
            String email = body.getJSONObject("kakao_account").optString("email", null);
            String nickname = body.getJSONObject("properties").optString("profile_nickname", null);

            return SocialUserInfo.builder()
                    .email(email)
                    .nickname(nickname)
                    .providerId(socialId)
                    .provider(Provider.KAKAO)
                    .build();
        } catch (org.json.JSONException e) {
            throw new RuntimeException("JSON 파싱 실패: " + e.getMessage(), e);
        }
    }
}
