package com.spig.spig.domain.learning.service;

import com.spig.spig.domain.learning.dto.ChunkUploadRequestDto;
import com.spig.spig.domain.learning.dto.ChunkUploadResponseDto;
import com.spig.spig.domain.learning.entity.ChunkUpload;
import com.spig.spig.domain.learning.repository.ChunkUploadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChunkUploadServiceImpl implements ChunkUploadService{

    private final ChunkUploadRepository uploadRepository;
    private final long chunkSize = 32000;


    @Override
    public ChunkUploadResponseDto createSession(ChunkUploadRequestDto request) {

        if (uploadRepository.existsById(request.getUploadId())) {
            throw new IllegalArgumentException(
                    "이미 존재하는 업로드 ID입니다."
            );
        }

        validateRequest(request);

        String originalName = extractFileName(
                request.getOriginalName()
        );
        int totalChunks = calculateTotalChunks(
                request.getTotalSize()
        );

        ChunkUpload chunk = ChunkUpload.create(
                originalName,
                request.getContentType(),
                request.getTotalSize(),
                chunkSize,
                totalChunks
        );

        return ChunkUploadResponseDto.from(
                uploadRepository.save(chunk)
        );
    }

    private void validateRequest(ChunkUploadRequestDto request) {
        if (request == null) {
            throw new IllegalArgumentException(
                    "업로드 세션 요청이 비어 있습니다."
            );
        }

        if (!StringUtils.hasText(request.getOriginalName())) {
            throw new IllegalArgumentException(
                    "파일 이름은 필수입니다."
            );
        }

        if (request.getTotalSize() <= 0) {
            throw new IllegalArgumentException(
                    "파일 크기는 0보다 커야 합니다."
            );
        }
    }

    private String extractFileName(String originalName) {
        String cleanPath = StringUtils.cleanPath(originalName);
        String fileName = StringUtils.getFilename(cleanPath);

        if (!StringUtils.hasText(fileName)) {
            throw new IllegalArgumentException(
                    "올바른 파일 이름이 아닙니다."
            );
        }

        return fileName;
    }

    private int calculateTotalChunks(long totalSize) {
        long totalChunks = Math.floorDiv(
                totalSize - 1,
                chunkSize
        ) + 1;

        if (totalChunks > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(
                    "처리할 수 있는 청크 개수를 초과했습니다."
            );
        }

        return (int) totalChunks;
    }
}
