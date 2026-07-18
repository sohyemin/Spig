package com.spig.spig.learning.service;

import com.spig.spig.learning.dto.LearningRequestDto;
import com.spig.spig.learning.dto.LearningResponseDto;
import com.spig.spig.learning.entity.LearningContent;
import org.springframework.stereotype.Service;

import java.util.List;

public interface LearningService {

    // Create
    public LearningResponseDto save(LearningRequestDto dto);
    void saveAll(List<LearningRequestDto> request);

    // Read

    public List<LearningResponseDto> readAll();

    public LearningResponseDto readById(Long id);
    // Update

    public void modifyContent(Long id, LearningRequestDto dto);
    // Delete

    public void deleteContent(Long id);
}
