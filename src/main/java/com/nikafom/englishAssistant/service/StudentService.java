package com.nikafom.englishAssistant.service;

import com.nikafom.englishAssistant.model.dto.request.StudentInfoRequest;
import com.nikafom.englishAssistant.model.dto.response.StudentInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {
    public StudentInfoResponse createStudent(@Valid StudentInfoRequest request) {
        if(!request.getPhoneNumber().matches("^\\+79[0-9]{9}$")) {
            return null;
        }

        if(!EmailValidator.getInstance().isValid(request.getEmail())) {
            return null;
        }

        return StudentInfoResponse.builder()
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .interests(request.getInterests())
                .build();
    }

    public StudentInfoResponse getStudent(Long id) {
        return null;
    }

    public StudentInfoResponse updateStudent(Long id, @Valid StudentInfoRequest request) {
        if(!request.getPhoneNumber().matches("^\\+79[0-9]{9}$")) {
            return null;
        }

        if(!EmailValidator.getInstance().isValid(request.getEmail())) {
            return null;
        }

        return StudentInfoResponse.builder()
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .interests(request.getInterests())
                .build();
    }

    public void deleteStudent(Long id) {
    }

    public List<StudentInfoResponse> getAllStudents() {
        return Collections.emptyList();
    }
}
