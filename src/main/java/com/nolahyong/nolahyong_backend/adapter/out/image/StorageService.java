package com.nolahyong.nolahyong_backend.adapter.out.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    @Value("${custom.image.upload-path}")
    private String uploadDir;

    public String store(UUID userId, MultipartFile file) {
        try {
            // 디렉토리가 없으면 생성
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 원본 파일명 확보
            String originalFilename = Paths.get(file.getOriginalFilename()).getFileName().toString();

            // 저장될 파일명 구성
            String filename = userId + "_" + UUID.randomUUID() + "_" + originalFilename;

            // 저장 경로
            Path targetPath = uploadPath.resolve(filename).normalize();
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // 프론트에서 접근 가능한 URL 경로 반환
            return "/images/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public void delete(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) return;
        try {
            String filename = Paths.get(imageUrl).getFileName().toString();
            Path targetPath = Paths.get(uploadDir).resolve(filename);
            Files.deleteIfExists(targetPath);
        } catch (IOException e) {
            log.warn("이미지 삭제 실패: {}", imageUrl, e);
        }
    }
}