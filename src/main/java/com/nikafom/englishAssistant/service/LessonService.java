package com.nikafom.englishAssistant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikafom.englishAssistant.model.db.entity.Lesson;
import com.nikafom.englishAssistant.model.db.repository.LessonRepository;
import com.nikafom.englishAssistant.model.dto.request.LessonInfoRequest;
import com.nikafom.englishAssistant.model.dto.response.LessonInfoResponse;
import com.nikafom.englishAssistant.model.enums.LessonStatus;
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
public class LessonService {
    private final ObjectMapper mapper;
    private final LessonRepository lessonRepository;

    public LessonInfoResponse createLesson(LessonInfoRequest request) {
        Lesson lesson = mapper.convertValue(request, Lesson.class);
        lesson.setCreatedAt(LocalDateTime.now());
        lesson.setStatus(LessonStatus.PLANNED);

        Lesson savedLesson = lessonRepository.save(lesson);

        return mapper.convertValue(savedLesson, LessonInfoResponse.class);
    }

    public LessonInfoResponse getLesson(Long id) {
        Lesson lesson = getLessonFromDB(id);
        return mapper.convertValue(lesson, LessonInfoResponse.class);
    }

    public Lesson getLessonFromDB(Long id) {
        return lessonRepository.findById(id).orElse(new Lesson());
    }

    public LessonInfoResponse updateLesson(Long id, LessonInfoRequest request) {
        Lesson lesson = getLessonFromDB(id);

        lesson.setDate(request.getDate() == null ? lesson.getDate() : request.getDate());
        lesson.setTime(request.getTime() == null ? lesson.getTime() : request.getTime());
        lesson.setLessonDuration(request.getLessonDuration() == null ? lesson.getLessonDuration() : request.getLessonDuration());

        lesson.setUpdatedAt(LocalDateTime.now());
        lesson.setStatus(LessonStatus.RESCHEDULED);

        Lesson savedLesson = lessonRepository.save(lesson);

        return mapper.convertValue(savedLesson, LessonInfoResponse.class);
    }

    public void deleteLesson(Long id) {
        Lesson lesson = getLessonFromDB(id);
        lesson.setUpdatedAt(LocalDateTime.now());
        lesson.setStatus(LessonStatus.CANCELLED);
        lessonRepository.save(lesson);
    }

    public List<LessonInfoResponse> getAllLessons() {
        return lessonRepository.findAll().stream()
                .map(lesson -> mapper.convertValue(lesson, LessonInfoResponse.class))
                .collect(Collectors.toList());
    }
}
