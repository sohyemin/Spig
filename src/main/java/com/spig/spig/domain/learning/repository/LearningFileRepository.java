package com.spig.spig.domain.learning.repository;

import com.spig.spig.domain.learning.entity.LearningFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningFileRepository extends JpaRepository<LearningFile, Long> {
}
