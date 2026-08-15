package com.spig.spig.global.storage;

import com.spig.spig.domain.learning.dto.ChunkUploadRequestDto;
import com.spig.spig.domain.learning.entity.ChunkUpload;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.UUID;

public interface ChunkFileStorage {
    void saveChunk(
            ChunkUploadRequestDto request
    );

    Path mergeChunks(
            ChunkUpload upload
    );
}
