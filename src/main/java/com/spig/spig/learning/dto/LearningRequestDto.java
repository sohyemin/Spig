package com.spig.spig.learning.dto;

import com.spig.spig.learning.entity.Category;
import lombok.Getter;

@Getter
public class LearningRequestDto {
    private String title;
    private String content;
    private Category category;
}
