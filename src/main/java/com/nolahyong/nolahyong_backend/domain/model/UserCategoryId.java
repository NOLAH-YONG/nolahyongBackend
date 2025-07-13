package com.nolahyong.nolahyong_backend.domain.model;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserCategoryId implements Serializable {
    private UUID user;
    private String category;
}