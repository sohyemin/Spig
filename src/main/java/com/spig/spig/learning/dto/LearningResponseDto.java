package com.spig.spig.learning.dto;

import com.spig.spig.learning.entity.Category;
import com.spig.spig.learning.entity.LearningContent;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LearningResponseDto {
    private Long id;

    private String title;
    private String content;

    private Category category;

    public static LearningResponseDto to(LearningContent content) {
        return LearningResponseDto.builder()
                .id(content.getId())
                .title(content.getTitle())
                .content(content.getContent())
                .category(content.getCategory())
                .build();
    }
}
