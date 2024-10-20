package com.nikafom.englishAssistant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikafom.englishAssistant.model.db.entity.Student;
import com.nikafom.englishAssistant.model.db.entity.User;
import com.nikafom.englishAssistant.model.db.repository.StudentRepository;
import com.nikafom.englishAssistant.model.dto.request.StudentInfoRequest;
import com.nikafom.englishAssistant.model.dto.request.StudentToUserRequest;
import com.nikafom.englishAssistant.model.dto.response.StudentInfoResponse;
import com.nikafom.englishAssistant.model.enums.StudentStatus;
import com.nikafom.englishAssistant.utils.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {
    private final ObjectMapper mapper;
    private final StudentRepository studentRepository;
    private final UserService userService;

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

    public Page<StudentInfoResponse> getAllStudents(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);

        Page<Student> allStudents;
        if(filter == null) {
            allStudents = studentRepository.findAllByStatusNot(pageRequest, StudentStatus.DELETED);
        } else {
            allStudents = studentRepository.findAllByStatusNotFiltered(pageRequest, StudentStatus.DELETED, filter.toUpperCase());
        }

        List<StudentInfoResponse> content = allStudents.getContent().stream()
                .map(student -> mapper.convertValue(student, StudentInfoResponse.class))
                .collect(Collectors.toList());
        return new PageImpl<>(content, pageRequest, allStudents.getTotalElements());
    }

    public void addStudentToUser(StudentToUserRequest request) {
        Student student = studentRepository.findById(request.getStudentId()).orElse(null);
        if(student == null) {
            return;
        }

        User user = userService.getUserFromDB(request.getUserId());
        if(user == null) {
            return;
        }

        student.setUser(user);
        studentRepository.save(student);
    }

    public List<StudentInfoResponse> getUserStudents(Long id) {
        return studentRepository.findAllByUserId(id).stream()
                .map(student -> mapper.convertValue(student, StudentInfoResponse.class))
                .collect(Collectors.toList());
    }
}
