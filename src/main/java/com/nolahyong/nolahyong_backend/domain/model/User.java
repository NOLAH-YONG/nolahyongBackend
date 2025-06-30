package com.nolahyong.nolahyong_backend.domain.model;

import com.nolahyong.nolahyong_backend.domain.model.enums.Provider;
import com.nolahyong.nolahyong_backend.domain.model.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue
    private UUID userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(nullable = false, name = "provider_id")
    private String providerId;

    @Column
    private String profileImage; // 프로필 이미지 URL

    @Column
    private Boolean profileCompleted; // 온보딩 완료 여부

    @Column
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
}