package com.nolahyong.nolahyong_backend.application.service;

import com.nolahyong.nolahyong_backend.domain.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public boolean isOnboarded(User user) {
        return user.getProfileCompleted() != null && user.getProfileCompleted();
    }
}