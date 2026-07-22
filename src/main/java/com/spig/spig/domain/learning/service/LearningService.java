package com.spig.spig.domain.learning.service;

import com.spig.spig.domain.learning.dto.LearningRequestDto;
import com.spig.spig.domain.learning.dto.LearningResponseDto;

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
