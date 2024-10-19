package com.nikafom.englishAssistant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikafom.englishAssistant.model.db.entity.Homework;
import com.nikafom.englishAssistant.model.db.entity.Student;
import com.nikafom.englishAssistant.model.db.repository.HomeworkRepository;
import com.nikafom.englishAssistant.model.dto.request.HomeworkInfoRequest;
import com.nikafom.englishAssistant.model.dto.request.HomeworkToStudentRequest;
import com.nikafom.englishAssistant.model.dto.response.HomeworkInfoResponse;
import com.nikafom.englishAssistant.model.enums.HomeworkStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeworkService {
    private final ObjectMapper mapper;
    private final HomeworkRepository homeworkRepository;
    private final StudentService studentService;

    public HomeworkInfoResponse createHomework(HomeworkInfoRequest request) {
        Homework homework = mapper.convertValue(request, Homework.class);
        homework.setCreatedAt(LocalDateTime.now());
        homework.setStatus(HomeworkStatus.GIVEN);

        Homework savedHomework = homeworkRepository.save(homework);

        return mapper.convertValue(savedHomework, HomeworkInfoResponse.class);
    }

    public HomeworkInfoResponse getHomework(Long id) {
        Homework homework = getHomeworkFromDB(id);
        return mapper.convertValue(homework, HomeworkInfoResponse.class);
    }

    public Homework getHomeworkFromDB(Long id) {
        return homeworkRepository.findById(id).orElse(new Homework());
    }

    public HomeworkInfoResponse updateHomework(Long id, HomeworkInfoRequest request) {
        Homework homework = getHomeworkFromDB(id);

        homework.setTask(request.getTask());
        homework.setDate(request.getDate() == null ? homework.getDate() : request.getDate());

        homework.setUpdatedAt(LocalDateTime.now());
        homework.setStatus(HomeworkStatus.CHANGED);

        Homework savedHomework = homeworkRepository.save(homework);

        return mapper.convertValue(savedHomework, HomeworkInfoResponse.class);
    }

    public void deleteHomework(Long id) {
        Homework homework = getHomeworkFromDB(id);
        homework.setUpdatedAt(LocalDateTime.now());
        homework.setStatus(HomeworkStatus.DELETED);
        homeworkRepository.save(homework);
    }

    public List<HomeworkInfoResponse> getAllHomework() {
        return homeworkRepository.findAll().stream()
                .map(homework -> mapper.convertValue(homework, HomeworkInfoResponse.class))
                .collect(Collectors.toList());
    }

    public void addHomeworkToStudent(HomeworkToStudentRequest request) {
        Homework homework = homeworkRepository.findById(request.getHomeworkId()).orElse(null);
        if(homework == null) {
            return;
        }

        Student student = studentService.getStudentFromDB(request.getStudentId());
        if(student == null) {
            return;
        }

        homework.setStudent(student);
        homeworkRepository.save(homework);
    }

    public List<HomeworkInfoResponse> getStudentHomework(Long id) {
        return homeworkRepository.findAllByStudentId(id).stream()
                .map(homework -> mapper.convertValue(homework, HomeworkInfoResponse.class))
                .collect(Collectors.toList());
    }

    public void markDoneHomework(Long id) {
        Homework homework = homeworkRepository.findById(id).orElse(null);
        homework.setStatus(HomeworkStatus.DONE);
        homework.setUpdatedAt(LocalDateTime.now());
        homeworkRepository.save(homework);
    }

    public void markNotDoneHomework(Long id) {
        Homework homework = homeworkRepository.findById(id).orElse(null);
        homework.setStatus(HomeworkStatus.NOT_DONE);
        homework.setUpdatedAt(LocalDateTime.now());
        homeworkRepository.save(homework);
    }
}
