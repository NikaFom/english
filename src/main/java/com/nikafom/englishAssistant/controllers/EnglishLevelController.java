package com.nikafom.englishAssistant.controllers;

import com.nikafom.englishAssistant.model.dto.request.EnglishLevelInfoRequest;
import com.nikafom.englishAssistant.model.dto.response.EnglishLevelInfoResponse;
import com.nikafom.englishAssistant.service.EnglishLevelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Tag(name = "Уровни английского")
@RestController
@RequestMapping("/api/englishLevels")
@RequiredArgsConstructor
public class EnglishLevelController {

    private final EnglishLevelService englishLevelService;

    @PostMapping
    @Operation(summary = "Создать уровень английского")
    public EnglishLevelInfoResponse createEnglishLevel(@RequestBody EnglishLevelInfoRequest request) {
        return englishLevelService.createEnglishLevel(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить уровень английского по ID")
    public EnglishLevelInfoResponse getEnglishLevel(@PathVariable Long id) {
        return englishLevelService.getEnglishLevel(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить уровень английского по ID")
    public EnglishLevelInfoResponse updateEnglishLevel(@PathVariable Long id, @RequestBody EnglishLevelInfoRequest request) {
        return englishLevelService.updateEnglishLevel(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить уровень английского по ID")
    public void deleteEnglishLevel(@PathVariable Long id) {
        englishLevelService.deleteEnglishLevel(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить список уровней английского")
    public List<EnglishLevelInfoResponse> getAllEnglishLevels() {
        return englishLevelService.getAllEnglishLevels();
    }
}
