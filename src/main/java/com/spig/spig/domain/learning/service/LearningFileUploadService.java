package com.spig.spig.domain.learning.service;

import com.spig.spig.domain.learning.dto.UploadResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LearningFileUploadService {

    // Create
    UploadResponseDto upload(MultipartFile file);

    // Read
    UploadResponseDto findById(Long fileId);

    List<UploadResponseDto> findAll();

    // Update: 기존 파일을 새로운 파일로 교체
    UploadResponseDto update(
            Long fileId,
            MultipartFile file
    );

    // Delete: 실제 파일과 DB 정보를 함께 삭제
    void delete(Long fileId);
}
