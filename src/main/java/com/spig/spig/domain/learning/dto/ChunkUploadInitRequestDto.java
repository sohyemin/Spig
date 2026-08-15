package com.spig.spig.domain.learning.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class ChunkUploadInitRequestDto {

    @NotBlank
    private String originalName;

    @NotBlank
    private String contentType;

    @Positive
    private long totalSize;
}
