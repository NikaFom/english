package com.nikafom.englishAssistant.controllers;

import com.nikafom.englishAssistant.model.dto.request.StudentInfoRequest;
import com.nikafom.englishAssistant.model.dto.request.StudentToUserRequest;
import com.nikafom.englishAssistant.model.dto.response.StudentInfoResponse;
import com.nikafom.englishAssistant.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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
    public Page<StudentInfoResponse> getAllStudents(@RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer perPage,
                                                    @RequestParam(defaultValue = "name") String sort,
                                                    @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                                    @RequestParam(required = false) String filter
    ) {
        return studentService.getAllStudents(page, perPage, sort, order, filter);
    }

    @PostMapping("/studentToUser")
    @Operation(summary = "Добавить ученика пользователю")
    public void addStudentToUser(@RequestBody StudentToUserRequest request) {
        studentService.addStudentToUser(request);
    }

    @GetMapping("/userStudents/{id}")
    @Operation(summary = "Получить список учеников пользователя по ID")
    public List<StudentInfoResponse> getUserStudents(@PathVariable Long id) {
        return studentService.getUserStudents(id);
    }
}
