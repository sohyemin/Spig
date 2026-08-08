package com.spig.spig.domain.learning.dto;

import com.spig.spig.domain.learning.entity.FileUploadStatus;
import com.spig.spig.domain.learning.entity.LearningFile;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UploadResponseDto {

    private Long fileId;
    private String originalName;
    private long size;
    private FileUploadStatus status;
    private LocalDateTime createdAt;

    public static UploadResponseDto to(
            LearningFile learningFile
    ) {
        return UploadResponseDto.builder()
                .fileId(learningFile.getId())
                .originalName(
                        learningFile.getOriginalName()
                )
                .size(learningFile.getSize())
                .status(learningFile.getStatus())
                .createdAt(learningFile.getCreatedAt())
                .build();
    }
}