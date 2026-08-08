package com.spig.spig.domain.learning.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Entity
@Table(name = "learning_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LearningFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false, unique = true)
    private String storedName;

    @Column(nullable = false, length = 1000)
    private String storagePath;

    private String contentType;

    @Column(name = "file_size", nullable = false)
    private long size;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileUploadStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static LearningFile from(
            String originalName,
            String storedName,
            String storagePath,
            String contentType,
            long size
    ) {
        return LearningFile.builder()
                .originalName(originalName)
                .storedName(storedName)
                .storagePath(storagePath)
                .contentType(contentType)
                .size(size)
                .build();
    }

    public void replace(
            String originalName,
            String storedName,
            String storagePath,
            String contentType,
            long size
    ) {
        this.originalName = originalName;
        this.storedName = storedName;
        this.storagePath = storagePath;
        this.contentType = contentType;
        this.size = size;
        this.status = FileUploadStatus.UPLOADING;
    }

    public void markSuccess() {
        this.status = FileUploadStatus.SUCCESS;
    }

    public void markFailed() {
        this.status = FileUploadStatus.FAILED;
    }
}