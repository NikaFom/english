package com.nikafom.englishAssistant.controllers;

import com.nikafom.englishAssistant.model.dto.request.HomeworkInfoRequest;
import com.nikafom.englishAssistant.model.dto.response.HomeworkInfoResponse;
import com.nikafom.englishAssistant.service.HomeworkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Tag(name = "Домашние задания")
@RestController
@RequestMapping("/api/homework")
@RequiredArgsConstructor
public class HomeworkController {

    private final HomeworkService homeworkService;

    @PostMapping
    @Operation(summary = "Создать домашнее задание")
    public HomeworkInfoResponse createHomework(@RequestBody HomeworkInfoRequest request) {
        return homeworkService.createHomework(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить домашнее задание по ID")
    public HomeworkInfoResponse getHomework(@PathVariable Long id) {
        return homeworkService.getHomework(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить домашнее задание по ID")
    public HomeworkInfoResponse updateHomework(@PathVariable Long id, @RequestBody HomeworkInfoRequest request) {
        return homeworkService.updateHomework(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить домашнее задание по ID")
    public void deleteHomework(@PathVariable Long id) {
        homeworkService.deleteHomework(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить список домашних заданий")
    public List<HomeworkInfoResponse> getAllHomework() {
        return homeworkService.getAllHomework();
    }
}
