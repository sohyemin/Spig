package com.spig.spig.domain.learning.controller;

import com.spig.spig.domain.learning.dto.ChunkUploadInitRequestDto;
import com.spig.spig.domain.learning.dto.ChunkUploadInitResponseDto;
import com.spig.spig.domain.learning.dto.UploadResponseDto;
import com.spig.spig.domain.learning.service.ChunkUploadService;
import com.spig.spig.domain.learning.service.LearningFileUploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/admin/learning/files")
@RequiredArgsConstructor
public class AdminLearningFileController {

    private final LearningFileUploadService uploadService;
    private final ChunkUploadService chunkUploadService;

    // 일반 업로드
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadResponseDto upload(
            @RequestParam("file") MultipartFile file
    ) {
        log.info("파일 업로드 시작");
        return uploadService.upload(file);
    }

    // 청크 업로드
    @PostMapping("/uploads")
    public ResponseEntity<ChunkUploadInitResponseDto> chunkInit(
            @Valid @RequestBody
            ChunkUploadInitRequestDto request
    ) {
        log.info("청크 파일 업로드 시작");

        ChunkUploadInitResponseDto response = chunkUploadService.createSession(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/uploads/{uploadId}/{chunkIndex}")
    public UploadResponseDto uploadChunk(
            @RequestParam("file") MultipartFile file
    ) {
        return null;
    }

    @PutMapping("/uploads/{uploadId}/{chunkIndex}/complete")
    public UploadResponseDto uploadComplete(
            @RequestParam("file") MultipartFile file
    ) {
        return null;
    }
}