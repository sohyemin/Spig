package com.spig.spig.domain.learning.controller;

import com.spig.spig.domain.learning.dto.UploadResponseDto;
import com.spig.spig.domain.learning.service.LearningFileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/admin/learning/files")
@RequiredArgsConstructor
public class AdminLearningFileController {

    private final LearningFileUploadService uploadService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadResponseDto upload(
            @RequestParam("file") MultipartFile file
    ) {
        log.info("파일 업로드 시작");
        return uploadService.upload(file);
    }
}
