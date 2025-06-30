package com.nolahyong.nolahyong_backend.adapter.in.web.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class BasicResponse {
    private boolean success;
    private String message;
}