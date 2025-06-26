package com.nolahyong.nolahyong_backend.adapter.in.web;

import com.nolahyong.nolahyong_backend.application.dto.UserInfo;
import com.nolahyong.nolahyong_backend.security.CustomUserPrincipal;
import com.nolahyong.nolahyong_backend.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/me")
    public ResponseEntity<UserInfo> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();
        return ResponseEntity.ok(UserInfo.from(user));
    }
}