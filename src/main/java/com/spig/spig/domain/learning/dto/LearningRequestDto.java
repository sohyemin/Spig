package com.spig.spig.domain.learning.dto;

import com.spig.spig.domain.learning.entity.Category;
import lombok.Getter;

@Getter
public class LearningRequestDto {
    private String title;
    private String content;
    private Category category;
}
