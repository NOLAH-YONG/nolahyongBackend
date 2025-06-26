package com.nolahyong.nolahyong_backend.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    private SnsProvider provider;

    private String providerId;

    @Setter
    private String email;
    @Setter
    private String nickname;
    @Setter
    private String profileImageUrl;

    // 7. AccountStatus 필드 추가
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

}