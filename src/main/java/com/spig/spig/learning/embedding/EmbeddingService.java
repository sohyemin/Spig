package com.spig.spig.learning.embedding;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;
}
