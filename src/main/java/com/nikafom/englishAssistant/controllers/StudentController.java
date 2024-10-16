package com.nikafom.englishAssistant.controllers;

import com.nikafom.englishAssistant.model.dto.request.StudentInfoRequest;
import com.nikafom.englishAssistant.model.dto.response.StudentInfoResponse;
import com.nikafom.englishAssistant.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Ученики")
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @Operation(summary = "Создать ученика")
    public StudentInfoResponse createStudent(@RequestBody @Valid StudentInfoRequest request) {
        return studentService.createStudent(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить ученика по ID")
    public StudentInfoResponse getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить ученика по ID")
    public StudentInfoResponse updateStudent(@PathVariable Long id, @RequestBody @Valid StudentInfoRequest request) {
        return studentService.updateStudent(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить ученика по ID")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить список учеников")
    public List<StudentInfoResponse> getAllStudents() {
        return studentService.getAllStudents();
    }
}
