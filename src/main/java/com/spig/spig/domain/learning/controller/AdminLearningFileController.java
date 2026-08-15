package com.spig.spig.domain.learning.controller;

import com.spig.spig.domain.learning.dto.UploadResponseDto;
import com.spig.spig.domain.learning.service.LearningFileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/admin/learning/files")
@RequiredArgsConstructor
public class AdminLearningFileController {

    private final LearningFileUploadService uploadService;

    // 일반 업로드
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadResponseDto upload(
            @RequestParam("file") MultipartFile file
    ) {
        log.info("파일 업로드 시작");
        return uploadService.upload(file);
    }

    // 청크 업로드
    @PostMapping(value = "/uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadResponseDto chunkInit(
            @RequestParam("file") MultipartFile file
    ) {
        log.info("청크 파일 업로드 시작");
        return uploadService.upload(file);
    }

    @PutMapping(value = "/uploads/${uploadId}/${chunkIndex}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadResponseDto uploadChunk(
            @RequestParam("file") MultipartFile file
    ) {
        return null;
    }

    @PutMapping(value = "/uploads/${uploadId}/${chunkIndex}/complete", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadResponseDto uploadComplete(
            @RequestParam("file") MultipartFile file
    ) {
        return null;
    }
}