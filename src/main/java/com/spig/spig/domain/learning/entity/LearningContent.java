package com.spig.spig.domain.learning.entity;

import com.spig.spig.domain.learning.dto.LearningRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@Entity
public class LearningContent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;

    public static LearningContent from(LearningRequestDto dto) {
        return LearningContent.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .category(dto.getCategory())
                .embeddingStatus(EmbeddingStatus.PENDING)
                .build();}

    public void update(LearningRequestDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.category = dto.getCategory();
    }

    @Enumerated(EnumType.STRING)
    private EmbeddingStatus embeddingStatus;

    public void changeEmbeddingStatus(EmbeddingStatus embeddingStatus) {
        this.embeddingStatus = embeddingStatus;
    }
}
