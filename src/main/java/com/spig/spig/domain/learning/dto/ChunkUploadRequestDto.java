package com.spig.spig.domain.learning.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class ChunkUploadRequestDto {

    private UUID uploadId;

    private int chunkNumber;

    private MultipartFile chunk;
}
