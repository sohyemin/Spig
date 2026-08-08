package com.spig.spig.domain.learning.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class LearningFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private String originalName;
    private String storedName;

    private String storagePath;

    private String contentType;

    private long size;

    private FileUploadStatus status;
    private LocalDateTime createdAt;
}
