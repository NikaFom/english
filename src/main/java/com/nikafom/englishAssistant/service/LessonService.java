package com.nikafom.englishAssistant.service;

import com.nikafom.englishAssistant.model.dto.request.LessonInfoRequest;
import com.nikafom.englishAssistant.model.dto.response.LessonInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.DateValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LessonService {
    public LessonInfoResponse createLesson(LessonInfoRequest request) {
        return LessonInfoResponse.builder()
                .date(request.getDate())
                .time(request.getTime())
                .lessonDuration(request.getLessonDuration())
                .status(request.getStatus())
                .build();
    }

    public LessonInfoResponse getLesson(Long id) {
        return null;
    }

    public LessonInfoResponse updateLesson(Long id, LessonInfoRequest request) {
        return LessonInfoResponse.builder()
                .date(request.getDate())
                .time(request.getTime())
                .lessonDuration(request.getLessonDuration())
                .status(request.getStatus())
                .build();
    }

    public void deleteLesson(Long id) {
    }

    public List<LessonInfoResponse> getAllLessons() {
        return Collections.emptyList();
    }
}
