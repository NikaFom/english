package com.nikafom.englishAssistant.controllers;

import com.nikafom.englishAssistant.model.dto.request.HomeworkToLessonRequest;
import com.nikafom.englishAssistant.model.dto.request.LessonInfoRequest;
import com.nikafom.englishAssistant.model.dto.request.LessonToStudentRequest;
import com.nikafom.englishAssistant.model.dto.response.HomeworkInfoResponse;
import com.nikafom.englishAssistant.model.dto.response.LessonInfoResponse;
import com.nikafom.englishAssistant.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

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
    public Page<LessonInfoResponse> getAllLessons(@RequestParam(defaultValue = "1") Integer page,
                                                  @RequestParam(defaultValue = "10") Integer perPage,
                                                  @RequestParam(defaultValue = "date") String sort,
                                                  @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                                  @RequestParam(required = false) String filter
    ) {
        return lessonService.getAllLessons(page, perPage, sort, order, filter);
    }

    @PostMapping("/lessonToStudent")
    @Operation(summary = "Добавить занятие ученику")
    public void addLessonToStudent(@RequestBody LessonToStudentRequest request) {
        lessonService.addLessonToStudent(request);
    }

    @GetMapping("/studentLessons/{id}")
    @Operation(summary = "Получить список занятий ученика по ID")
    public List<LessonInfoResponse> getStudentLessons(@PathVariable Long id) {
        return lessonService.getStudentLessons(id);
    }

    @PostMapping("/homeworkToLesson")
    @Operation(summary = "Добавить домашнее задание к занятию")
    public void addHomeworkToLesson(@RequestBody HomeworkToLessonRequest request) {
        lessonService.addHomeworkToLesson(request);
    }

    @GetMapping("/lessonHomework/{id}")
    @Operation(summary = "Получить домашнее задание к занятию по ID")
    public HomeworkInfoResponse getLessonHomework(@PathVariable Long id) {
        return lessonService.getLessonHomework(id);
    }

    @PutMapping("/givenLesson/{id}")
    @Operation(summary = "Отметить занятие проведенным по ID")
    public void markGivenLesson(@PathVariable Long id) {
        lessonService.markGivenLesson(id);
    }

    @PutMapping("/paidLesson/{id}")
    @Operation(summary = "Отметить занятие оплаченным по ID")
    public void markPaidLesson(@PathVariable Long id) {
        lessonService.markPaidLesson(id);
    }
}
