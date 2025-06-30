package com.nolahyong.nolahyong_backend.application.service.social;

import com.nolahyong.nolahyong_backend.application.dto.SocialUserInfo;
import com.nolahyong.nolahyong_backend.domain.model.User;
import com.nolahyong.nolahyong_backend.domain.model.enums.Provider;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NaverAuthService implements SocialAuthProvider {

    private static final String USER_INFO_URL = "https://openapi.naver.com/v1/nid/me";

    @Override
    public SocialUserInfo authenticate(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        ResponseEntity<String> response = new RestTemplate().exchange(
                USER_INFO_URL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        try {
            JSONObject body = new JSONObject(response.getBody());
            String socialId = body.getString("id");
            String email = body.getJSONObject("kakao_account").optString("email", null);
            String nickname = body.getJSONObject("properties").optString("nickname", null);

            return SocialUserInfo.builder()
                    .email(email)
                    .nickname(nickname)
                    .providerId(socialId)
                    .provider(Provider.NAVER)
                    .build();
        } catch (org.json.JSONException e) {
            throw new RuntimeException("JSON 파싱 실패: " + e.getMessage(), e);
        }
    }
}
