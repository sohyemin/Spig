package com.spig.spig.learning.embedding;

import com.spig.spig.learning.entity.EmbeddingStatus;
import com.spig.spig.learning.entity.LearningContent;
import com.spig.spig.learning.repository.LearningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class EmbeddingService {

    private final LearningRepository learningRepository;
    private final VectorStore vectorStore;

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void embeddingContents(){
        List<LearningContent> targets =
                learningRepository.findTop100ByEmbeddingStatus(
                        EmbeddingStatus.PENDING
                );

        if (targets.isEmpty()) {
            return;
        }

        //중복 제거
        deleteVectors(targets);

        List<Document> documents = targets.stream()
                .map(this::toDocument)
                .toList();

        vectorStore.add(documents);

        targets.forEach(content -> {
            content.changeEmbeddingStatus(EmbeddingStatus.COMPLETED);
        });
    }

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void deleteContents(){
        List<LearningContent> targets =
                learningRepository.findTop100ByEmbeddingStatus(
                        EmbeddingStatus.DELETE_PENDING
                );

        if (targets.isEmpty()) {
            return;
        }

        deleteVectors(targets);

        learningRepository.deleteAll(targets);
    }

    private Document toDocument(LearningContent content) {
        return new Document(
                content.getContent(),
                Map.of(
                        "learningContentId", content.getId().toString(),
                        "title", content.getTitle(),
                        "category", content.getCategory().name()
                )
        );
    }

    private void deleteVectors(List<LearningContent> targets){
        FilterExpressionBuilder builder = new FilterExpressionBuilder();

        for(LearningContent content:targets){
            vectorStore.delete(
                    builder.eq(
                            "learningContentId",
                            content.getId().toString()
                    ).build()
            );
        }
    }
}
