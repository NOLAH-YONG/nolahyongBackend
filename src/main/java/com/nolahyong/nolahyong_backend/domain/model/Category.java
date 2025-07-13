package com.nolahyong.nolahyong_backend.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @Column(name = "category_code", length = 50)
    private String code;

    private String groupCode;

    private String nameEn;

    @Column(nullable = false)
    private String nameKo;

    private Integer sortOrder;

}

