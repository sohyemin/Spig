package com.spig.spig.domain.learning.service;

import com.spig.spig.domain.learning.dto.ChunkUploadInitRequestDto;
import com.spig.spig.domain.learning.dto.ChunkUploadInitResponseDto;
import com.spig.spig.domain.learning.dto.ChunkUploadRequestDto;
import com.spig.spig.domain.learning.dto.ChunkUploadResponseDto;
import com.spig.spig.domain.learning.entity.ChunkUpload;
import com.spig.spig.domain.learning.entity.FileUploadStatus;
import com.spig.spig.domain.learning.repository.ChunkUploadRepository;
import com.spig.spig.global.exception.CustomException;
import com.spig.spig.global.storage.ChunkFileStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;

import static com.spig.spig.global.exception.ErrorCode.FILE_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChunkUploadServiceImpl implements ChunkUploadService{

    private final ChunkUploadRepository uploadRepository;
    private final ChunkFileStorage chunkFileStorage;

    @Value("${file.upload.chunk-size}")private int chunkSize;

    private final Set<String> ALLOWED_EXTENSIONS =
            Set.of(
                    "pdf",
                    "png",
                    "jpg",
                    "jpeg",
                    "gif",
                    "webp"
            );

    @Override
    public ChunkUploadInitResponseDto createSession(ChunkUploadInitRequestDto request) {

        validateRequest(request);

        ChunkUpload session = ChunkUpload.create(request, chunkSize);
        uploadRepository.save(session);

        return ChunkUploadInitResponseDto.from(session);
    }

    @Transactional
    @Override
    public void uploadChunk(ChunkUploadRequestDto request) {
        ChunkUpload upload = uploadRepository
                .findById(request.getUploadId())
                .orElseThrow(()-> new CustomException(FILE_NOT_FOUND));

        validateChunkNumber(upload, request.getChunkNumber());
        validateChunkSize(upload, request.getChunkNumber(), request.getChunk());

        chunkFileStorage.saveChunk(request);

        upload.upload();
    }

    @Transactional
    @Override
    public void complete(UUID uploadId) {
        ChunkUpload upload = uploadRepository
                .findById(uploadId)
                .orElseThrow(()-> new CustomException(FILE_NOT_FOUND));


        System.out.println(upload.getStatus());

        if (upload.getStatus()
                != FileUploadStatus.UPLOADING) {
            throw new IllegalStateException(
                    "완료할 수 없는 업로드 상태입니다."
            );
        }

        chunkFileStorage.mergeChunks(upload);
        upload.success();

        //파일 삭제
        deleteChunksAfterCommit(uploadId);
    }

    private void deleteChunksAfterCommit(UUID uploadId) {
        TransactionSynchronizationManager
                .registerSynchronization(
                        new TransactionSynchronization() {

                            @Override
                            public void afterCommit() {
                                try {
                                    chunkFileStorage
                                            .deleteChunks(
                                                    uploadId
                                            );
                                } catch (RuntimeException exception) {
                                    log.warn(
                                            "임시 청크 삭제 실패. uploadId={}",
                                            uploadId,
                                            exception
                                    );
                                }
                            }
                        }
                );
    }

    private void validateChunkSize(ChunkUpload upload, int chunkNumber, MultipartFile chunk) {
        long expectedSize;

        boolean lastChunk =
                chunkNumber
                        == upload.getTotalChunks() - 1;

        if (lastChunk) {
            expectedSize =
                    upload.getTotalSize()
                            - upload.getChunkSize()
                            * (upload.getTotalChunks() - 1L);
        } else {
            expectedSize = upload.getChunkSize();
        }

        if (chunk.getSize() != expectedSize) {
            throw new IllegalArgumentException(
                    "청크 크기가 올바르지 않습니다."
            );
        }
    }

    private void validateChunkNumber(ChunkUpload upload, int chunkNumber) {
        if (
                chunkNumber < 0
                        || chunkNumber >= upload.getTotalChunks()
        ) {
            throw new IllegalArgumentException(
                    "유효하지 않은 청크 번호입니다."
            );
        }
    }

    private void validateRequest(ChunkUploadInitRequestDto request) {

        if (!StringUtils.hasText(request.getOriginalName())) {
            throw new IllegalArgumentException(
                    "파일 이름은 필수입니다."
            );
        }

        String extension =
                StringUtils.getFilenameExtension(
                        request.getOriginalName()
                );

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException(
                    "허용되지 않은 파일 확장자입니다."
            );
        }

        if (request.getTotalSize() <= 0) {
            throw new IllegalArgumentException(
                    "파일 크기는 0보다 커야 합니다."
            );
        }
    }
}
