package com.nolahyong.nolahyong_backend.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Provider {
    KAKAO("kakao"),
    GOOGLE("google"),
    NAVER("naver");

    private final String value;

    Provider(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Provider fromValue(String value) {
        for (Provider provider : Provider.values()) {
            if (provider.value.equalsIgnoreCase(value)) {
                return provider;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 소셜 로그인 타입: " + value);
    }
}