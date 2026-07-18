package com.spig.spig.ai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RagServiceImpl implements RagService{

    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    @Override
    public String search(String message) {
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(message)
                        .similarityThreshold(0.75)
                        .topK(3)
                        .build()
        );

        String content = chatClient.prompt()
                .system("""
                        다음 학습 자료를 참고해서 답변하세요.

                        학습 자료:
                        %s
                      
                        모르면 모른다고 답변하세요.
                        """.formatted(documents))
                .user(message)
                .call()
                .content();

        return content;
    }
}
