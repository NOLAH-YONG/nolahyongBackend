package com.nolahyong.nolahyong_backend.domain.repository;

import com.nolahyong.nolahyong_backend.domain.model.User;
import com.nolahyong.nolahyong_backend.domain.model.UserCategory;
import com.nolahyong.nolahyong_backend.domain.model.UserCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCategoryRepository extends JpaRepository<UserCategory, UserCategoryId> {
    List<UserCategory> findByUser(User user);

    void deleteByUser(User user);
}