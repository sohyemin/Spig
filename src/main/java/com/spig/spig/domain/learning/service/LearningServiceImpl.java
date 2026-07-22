package com.spig.spig.domain.learning.service;

import com.spig.spig.domain.learning.dto.LearningRequestDto;
import com.spig.spig.domain.learning.dto.LearningResponseDto;
import com.spig.spig.domain.learning.entity.EmbeddingStatus;
import com.spig.spig.domain.learning.entity.LearningContent;
import com.spig.spig.domain.learning.repository.LearningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LearningServiceImpl implements LearningService{

    private final LearningRepository learningRepository;

    @Override
    @Transactional
    public LearningResponseDto save(LearningRequestDto dto) {
        LearningContent saved = learningRepository.save(LearningContent.from(dto));
        return LearningResponseDto.to(saved);
    }

    @Override
    @Transactional
    public void saveAll(List<LearningRequestDto> request) {
        List<LearningContent> contents = request.stream()
                .map(LearningContent::from)
                .toList();

        learningRepository.saveAll(contents);
    }

    @Override
    public List<LearningResponseDto> readAll() {
        return learningRepository.findAll().stream().map(
                LearningResponseDto::to).toList();
    }

    @Override
    public LearningResponseDto readById(Long id) {
        return LearningResponseDto.to(getContent(id));
    }

    @Override
    @Transactional
    public void modifyContent(Long id, LearningRequestDto dto) {
        LearningContent content = getContent(id);
        content.update(dto);
        content.changeEmbeddingStatus(EmbeddingStatus.PENDING);
    }

    @Override
    @Transactional
    public void deleteContent(Long id) {
        getContent(id).changeEmbeddingStatus(EmbeddingStatus.DELETE_PENDING);
    }


    public LearningContent getContent(Long id){
        return learningRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학습데이터입니다."));
    }
}
