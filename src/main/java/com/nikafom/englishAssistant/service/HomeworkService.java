package com.nikafom.englishAssistant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikafom.englishAssistant.model.db.entity.Homework;
import com.nikafom.englishAssistant.model.db.repository.HomeworkRepository;
import com.nikafom.englishAssistant.model.dto.request.HomeworkInfoRequest;
import com.nikafom.englishAssistant.model.dto.response.HomeworkInfoResponse;
import com.nikafom.englishAssistant.model.enums.HomeworkStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeworkService {
    private final ObjectMapper mapper;
    private final HomeworkRepository homeworkRepository;

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
}
