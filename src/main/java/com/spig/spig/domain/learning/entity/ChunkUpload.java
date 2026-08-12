package com.spig.spig.domain.learning.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
            String originalName,
            String contentType,
            long totalSize,
            long chunkSize,
            int totalChunks
    ) {
        return ChunkUpload.builder()
                .uploadId(UUID.randomUUID())
                .originalName(originalName)
                .contentType(contentType)
                .totalSize(totalSize)
                .chunkSize(chunkSize)
                .totalChunks(totalChunks)
                .status(FileUploadStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();
    }
}