package com.nolahyong.nolahyong_backend.domain.repository;

import com.nolahyong.nolahyong_backend.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {
    List<Category> findByCodeIn(List<String> codes);
}