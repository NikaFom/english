package com.nikafom.englishAssistant.controllers;

import com.nikafom.englishAssistant.model.dto.request.HomeworkInfoRequest;
import com.nikafom.englishAssistant.model.dto.request.HomeworkToStudentRequest;
import com.nikafom.englishAssistant.model.dto.response.HomeworkInfoResponse;
import com.nikafom.englishAssistant.service.HomeworkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

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
    public Page<HomeworkInfoResponse> getAllHomework(@RequestParam(defaultValue = "1") Integer page,
                                                     @RequestParam(defaultValue = "10") Integer perPage,
                                                     @RequestParam(defaultValue = "date") String sort,
                                                     @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                                     @RequestParam(required = false) String filter
    ) {
        return homeworkService.getAllHomework(page, perPage, sort, order, filter);
    }

    @PostMapping("/homeworkToStudent")
    @Operation(summary = "Добавить домашнее задание ученику")
    public void addHomeworkToStudent(@RequestBody HomeworkToStudentRequest request) {
        homeworkService.addHomeworkToStudent(request);
    }

    @GetMapping("/studentHomework/{id}")
    @Operation(summary = "Получить список домашних заданий ученика по ID")
    public List<HomeworkInfoResponse> getStudentHomework(@PathVariable Long id) {
        return homeworkService.getStudentHomework(id);
    }

    @PutMapping("/doneHomework/{id}")
    @Operation(summary = "Отметить домашнее задание выполненным по ID")
    public void markDoneHomework(@PathVariable Long id) {
        homeworkService.markDoneHomework(id);
    }

    @PutMapping("/notDoneHomework/{id}")
    @Operation(summary = "Отметить домашнее задание невыполненным по ID")
    public void markNotDoneHomework(@PathVariable Long id) {
        homeworkService.markNotDoneHomework(id);
    }
}
