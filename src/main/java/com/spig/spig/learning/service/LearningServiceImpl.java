package com.spig.spig.learning.service;

import com.spig.spig.learning.dto.LearningRequestDto;
import com.spig.spig.learning.dto.LearningResponseDto;
import com.spig.spig.learning.entity.LearningContent;
import com.spig.spig.learning.repository.LearningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LearningServiceImpl implements LearningService{

    private final LearningRepository learningRepository;
    private final VectorStore vectorStore;

    @Override
    @Transactional
    public LearningResponseDto save(LearningRequestDto dto) {
        LearningContent saved = learningRepository.save(LearningContent.from(dto));

        Document document = new Document(
                saved.getContent(),
                Map.of(
                        "learningContentId", saved.getId().toString(),
                        "title", saved.getTitle(),
                        "category", saved.getCategory()
                )
        );

        vectorStore.add(List.of(document));

        return LearningResponseDto.to(saved);
    }

    @Override
    @Transactional
    public void saveAll(List<LearningRequestDto> request) {
        List<Document> documents = new ArrayList<>();

        for (LearningRequestDto dto:request){
            LearningContent saved = learningRepository.save(LearningContent.from(dto));

            documents.add(
                    new Document(
                            saved.getContent(),
                            Map.of(
                                    "learningContentId", saved.getId().toString(),
                                    "title", saved.getTitle(),
                                    "category", saved.getCategory()
                            )
                    )
            );
        }

        vectorStore.add(documents);
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
    }

    @Override
    @Transactional
    public void deleteContent(Long id) {
        learningRepository.deleteById(id);
    }


    public LearningContent getContent(Long id){
        return learningRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학습데이터입니다."));
    }
}
