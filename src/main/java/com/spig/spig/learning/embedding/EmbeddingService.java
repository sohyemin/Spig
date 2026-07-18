package com.spig.spig.learning.embedding;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;

    public float[] CreateEmbedding(String text){
        return embeddingModel.embed(text);
    }

}
