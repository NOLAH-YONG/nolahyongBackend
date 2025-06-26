package com.nolahyong.nolahyong_backend.application.port.out;

import com.nolahyong.nolahyong_backend.domain.model.SnsProvider;
import com.nolahyong.nolahyong_backend.domain.model.User;

import java.util.Optional;

public interface LoadUserPort {
    Optional<User> findByProviderAndProviderId(SnsProvider provider, String providerId);
    User save(User user);
}