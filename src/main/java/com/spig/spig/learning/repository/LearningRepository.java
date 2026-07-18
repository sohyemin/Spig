package com.spig.spig.learning.repository;

import com.spig.spig.learning.entity.EmbeddingStatus;
import com.spig.spig.learning.entity.LearningContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LearningRepository extends JpaRepository<LearningContent, Long> {

    @Query("select c from LearningContent c where c.EmbeddingStatus = :stastus")
    public List<LearningContent> findTop100ByEmbeddingStatus(EmbeddingStatus status);

}
