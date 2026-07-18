package com.spig.spig.learning.embedding;

import com.spig.spig.learning.entity.EmbeddingStatus;
import com.spig.spig.learning.entity.LearningContent;
import com.spig.spig.learning.repository.LearningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class EmbeddingService {

    private final LearningRepository learningRepository;
    private final EmbeddingModel embeddingModel;
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

        List<Document> documents = targets.stream()
                .map(this::toDocument)
                .toList();

        vectorStore.add(documents);

        targets.forEach(content -> {
            content.changeEmbeddingStatus(EmbeddingStatus.COMPLETED);
        });
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
}
