package com.spig.spig.domain.learning.dto;

import com.spig.spig.domain.learning.entity.ChunkUpload;
import com.spig.spig.domain.learning.entity.FileUploadStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
public class ChunkUploadResponseDto {

    private UUID uploadId;
    private int chunkNumber;
    private long receivedSize;
    private int uploadedChunks;
    private int totalChunks;
    private int progress;
    FileUploadStatus status;

    public static ChunkUploadResponseDto from(ChunkUpload chunk){

        return ChunkUploadResponseDto.builder()
                .build();
    }
}
