package com.spig.spig.domain.learning.entity;

import com.spig.spig.domain.learning.dto.ChunkUploadInitRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@Entity
@Table(name = "chunk_upload_session")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChunkUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uploadId;

    @Column(nullable = false)
    private String originalName;

    private String contentType;

    @Column(nullable = false)
    private long totalSize;

    @Column(nullable = false)
    private long chunkSize;

    @Column(nullable = false)
    private int totalChunks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileUploadStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static ChunkUpload create(
            ChunkUploadInitRequestDto request,
            int chunkSize
    ) {

        int totalChunks = Math.toIntExact(
                Math.ceilDiv(
                        request.getTotalSize(),
                        chunkSize
                )
        );


        return ChunkUpload.builder()
                .uploadId(UUID.randomUUID())
                .originalName(request.getOriginalName())
                .contentType(request.getContentType())
                .totalSize(request.getTotalSize())
                .chunkSize(chunkSize)
                .totalChunks(totalChunks)
                .status(FileUploadStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();
    }
}