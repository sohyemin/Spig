package com.spig.spig.domain.learning.repository;

import com.spig.spig.domain.learning.entity.EmbeddingStatus;
import com.spig.spig.domain.learning.entity.LearningContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LearningRepository extends JpaRepository<LearningContent, Long> {

    public List<LearningContent> findTop100ByEmbeddingStatus(EmbeddingStatus status);

}
