package com.nolahyong.nolahyong_backend.adapter.in.web;

import com.nolahyong.nolahyong_backend.adapter.in.web.dto.UserProfileResponse;
import com.nolahyong.nolahyong_backend.application.port.in.UserUseCase;
import com.nolahyong.nolahyong_backend.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserUseCase userUseCase;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userUseCase.getMyProfile(user.getUserId()));
    }

    @PostMapping("/onboarding/complete")
    public ResponseEntity<Void> completeOnboarding(@AuthenticationPrincipal User user) {
        userUseCase.completeOnboarding(user.getUserId());
        return ResponseEntity.ok().build();
    }
}