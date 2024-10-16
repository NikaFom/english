package com.nikafom.englishAssistant.controllers;

import com.nikafom.englishAssistant.model.dto.request.LessonInfoRequest;
import com.nikafom.englishAssistant.model.dto.response.LessonInfoResponse;
import com.nikafom.englishAssistant.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Tag(name = "Занятия")
@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping
    @Operation(summary = "Создать занятие")
    public LessonInfoResponse createLesson(@RequestBody LessonInfoRequest request) {
        return lessonService.createLesson(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить занятие по ID")
    public LessonInfoResponse getLesson(@PathVariable Long id) {
        return lessonService.getLesson(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить занятие по ID")
    public LessonInfoResponse updateLesson(@PathVariable Long id, @RequestBody LessonInfoRequest request) {
        return lessonService.updateLesson(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить занятие по ID")
    public void deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить список занятий")
    public List<LessonInfoResponse> getAllLessons() {
        return lessonService.getAllLessons();
    }
}
