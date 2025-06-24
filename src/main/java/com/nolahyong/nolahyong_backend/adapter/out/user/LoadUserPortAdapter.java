package com.nolahyong.nolahyong_backend.adapter.out.user;

import com.nolahyong.nolahyong_backend.application.port.out.LoadUserPort;
import com.nolahyong.nolahyong_backend.domain.model.User;
import com.nolahyong.nolahyong_backend.domain.model.SnsProvider;
import com.nolahyong.nolahyong_backend.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoadUserPortAdapter implements LoadUserPort {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findByProviderAndProviderId(SnsProvider provider, String providerId) {
        return userRepository.findByProviderAndProviderId(provider, providerId);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}