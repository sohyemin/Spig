package com.spig.spig.domain.learning.repository;

import com.spig.spig.domain.learning.entity.ChunkUpload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChunkUploadRepository extends JpaRepository<ChunkUpload, UUID> {
}
