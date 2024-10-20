package com.nikafom.englishAssistant.controllers;

import com.nikafom.englishAssistant.model.dto.request.GoalInfoRequest;
import com.nikafom.englishAssistant.model.dto.request.GoalToStudentRequest;
import com.nikafom.englishAssistant.model.dto.response.GoalInfoResponse;
import com.nikafom.englishAssistant.service.GoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Цели")
@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @PostMapping
    @Operation(summary = "Создать цель")
    public GoalInfoResponse createGoal(@RequestBody GoalInfoRequest request) {
        return goalService.createGoal(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить цель по ID")
    public GoalInfoResponse getGoal(@PathVariable Long id) {
        return goalService.getGoal(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить цель по ID")
    public GoalInfoResponse updateGoal(@PathVariable Long id, @RequestBody GoalInfoRequest request) {
        return goalService.updateGoal(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить цель по ID")
    public void deleteGoal(@PathVariable Long id) {
        goalService.deleteGoal(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить список целей")
    public Page<GoalInfoResponse> getAllGoals(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer perPage,
                                              @RequestParam(defaultValue = "date") String sort,
                                              @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                              @RequestParam(required = false) String filter
    ) {
        return goalService.getAllGoals(page, perPage,sort, order, filter);
    }

    @PostMapping("/goalToStudent")
    @Operation(summary = "Добавить цель ученику")
    public void addGoalToStudent(@RequestBody GoalToStudentRequest request) {
        goalService.addGoalToStudent(request);
    }

    @GetMapping("/studentGoals/{id}")
    @Operation(summary = "Получить список целей ученика по ID")
    public List<GoalInfoResponse> getStudentGoals(@PathVariable Long id) {
        return goalService.getStudentGoals(id);
    }

    @PutMapping("/accomplishedGoal/{id}")
    @Operation(summary = "Отметить цель достигнутой по ID")
    public void markAccomplishedGoal(@PathVariable Long id) {
        goalService.markAccomplishedGoal(id);
    }
}
