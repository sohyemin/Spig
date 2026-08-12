package com.spig.spig.domain.learning.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ChunkUploadRequestDto {

    private UUID uploadId;

    private String originalName;

    private String contentType;

    private int totalSize;
}
