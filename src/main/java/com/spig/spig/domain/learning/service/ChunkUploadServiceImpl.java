package com.spig.spig.domain.learning.service;

import com.spig.spig.domain.learning.dto.ChunkUploadInitRequestDto;
import com.spig.spig.domain.learning.dto.ChunkUploadInitResponseDto;
import com.spig.spig.domain.learning.dto.ChunkUploadRequestDto;
import com.spig.spig.domain.learning.dto.ChunkUploadResponseDto;
import com.spig.spig.domain.learning.entity.ChunkUpload;
import com.spig.spig.domain.learning.repository.ChunkUploadRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChunkUploadServiceImpl implements ChunkUploadService{

    private final ChunkUploadRepository uploadRepository;
    @Value("${file.upload.chunk-size}")private int chunkSize;

    @Override
    public ChunkUploadInitResponseDto createSession(ChunkUploadInitRequestDto request) {

        validateRequest(request);

        ChunkUpload session = ChunkUpload.create(request, chunkSize);

        return ChunkUploadInitResponseDto.from(session);
    }

    private void validateRequest(ChunkUploadInitRequestDto request) {
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
}
