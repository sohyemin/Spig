package com.spig.spig.domain.learning.dto;

import com.spig.spig.domain.learning.entity.ChunkUpload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
public class ChunkUploadInitResponseDto {
    private UUID uploadId;
    private long chunkSize;
    private long totalChunks;

    public static ChunkUploadInitResponseDto from(ChunkUpload session){
        return ChunkUploadInitResponseDto.builder()
                .uploadId(session.getUploadId())
                .chunkSize(session.getChunkSize())
                .totalChunks(session.getTotalChunks())
                .build();
    }
}
