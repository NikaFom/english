package com.nikafom.englishAssistant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikafom.englishAssistant.model.db.entity.Student;
import com.nikafom.englishAssistant.model.db.repository.StudentRepository;
import com.nikafom.englishAssistant.model.dto.request.StudentInfoRequest;
import com.nikafom.englishAssistant.model.dto.response.StudentInfoResponse;
import com.nikafom.englishAssistant.model.enums.StudentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {
    private final ObjectMapper mapper;
    private final StudentRepository studentRepository;

    public StudentInfoResponse createStudent(@Valid StudentInfoRequest request) {
        if(!request.getPhoneNumber().matches("^\\+79[0-9]{9}$")) {
            return null;
        }

        if(!EmailValidator.getInstance().isValid(request.getEmail())) {
            return null;
        }

        Student student = mapper.convertValue(request, Student.class);
        student.setCreatedAt(LocalDateTime.now());
        student.setStatus(StudentStatus.CREATED);

        Student savedStudent = studentRepository.save(student);

        return mapper.convertValue(savedStudent, StudentInfoResponse.class);
    }

    public StudentInfoResponse getStudent(Long id) {
        Student student = getStudentFromDB(id);
        return mapper.convertValue(student, StudentInfoResponse.class);
    }

    public Student getStudentFromDB(Long id) {
        return studentRepository.findById(id).orElse(new Student());
    }

    public StudentInfoResponse updateStudent(Long id, @Valid StudentInfoRequest request) {
        if(!request.getPhoneNumber().matches("^\\+79[0-9]{9}$")) {
            return null;
        }

        if(!EmailValidator.getInstance().isValid(request.getEmail())) {
            return null;
        }

        Student student = getStudentFromDB(id);

        student.setName(request.getName());
        student.setPhoneNumber(request.getPhoneNumber());
        student.setEmail(request.getEmail());
        student.setInterests(request.getInterests() == null ? student.getInterests() : request.getInterests());

        student.setUpdatedAt(LocalDateTime.now());
        student.setStatus(StudentStatus.UPDATED);

        Student savedStudent = studentRepository.save(student);

        return mapper.convertValue(savedStudent, StudentInfoResponse.class);
    }

    public void deleteStudent(Long id) {
        Student student = getStudentFromDB(id);
        student.setUpdatedAt(LocalDateTime.now());
        student.setStatus(StudentStatus.DELETED);
        studentRepository.save(student);
    }

    public List<StudentInfoResponse> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(student -> mapper.convertValue(student, StudentInfoResponse.class))
                .collect(Collectors.toList());
    }
}
