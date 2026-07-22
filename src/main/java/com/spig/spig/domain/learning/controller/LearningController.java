package com.spig.spig.domain.learning.controller;

import com.spig.spig.domain.learning.dto.LearningRequestDto;
import com.spig.spig.domain.learning.dto.LearningResponseDto;
import com.spig.spig.domain.learning.service.LearningService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learning")
@RequiredArgsConstructor
public class LearningController {

    private final LearningService learningService;

    @PostMapping("")
    public LearningResponseDto learningContent(@RequestBody LearningRequestDto dto) {
        return learningService.save(dto);
    }

    @PostMapping("/bulk")
        public void learningContents(
                @RequestBody List<LearningRequestDto> request) {
        learningService.saveAll(request);
    }

    @GetMapping("")
    public List<LearningResponseDto> readContentAll(){
        return learningService.readAll();
    }

    @GetMapping("/{id}")
    public LearningResponseDto readContent(@PathVariable Long id){
        return learningService.readById(id);
    }

    @PutMapping("/{id}")
    public void putContent(@RequestBody LearningRequestDto dto, @PathVariable Long id){
        learningService.modifyContent(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteContent(@PathVariable Long id){
        learningService.deleteContent(id);
    }

}
