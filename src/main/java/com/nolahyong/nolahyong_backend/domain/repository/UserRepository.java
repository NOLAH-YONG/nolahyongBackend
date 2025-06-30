package com.nolahyong.nolahyong_backend.domain.repository;

import com.nolahyong.nolahyong_backend.domain.model.enums.Provider;
import com.nolahyong.nolahyong_backend.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByProviderAndProviderId(Provider provider, String providerId);
}