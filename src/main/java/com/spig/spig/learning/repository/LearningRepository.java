package com.spig.spig.learning.repository;

import com.spig.spig.learning.entity.LearningContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningRepository extends JpaRepository<LearningContent, Long> {

}
