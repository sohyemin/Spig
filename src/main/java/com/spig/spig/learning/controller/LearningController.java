package com.spig.spig.learning.controller;

import com.spig.spig.learning.dto.LearningRequestDto;
import com.spig.spig.learning.dto.LearningResponseDto;
import com.spig.spig.learning.entity.LearningContent;
import com.spig.spig.learning.service.LearningService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learning")
@RequiredArgsConstructor
public class LearningController {

    private final LearningService learningService;

    @PostMapping("")
    public void learningContent(@RequestBody LearningRequestDto dto) {
        learningService.save(dto);
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
