package com.nikafom.englishAssistant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikafom.englishAssistant.exceptions.CustomException;
import com.nikafom.englishAssistant.model.db.entity.Homework;
import com.nikafom.englishAssistant.model.db.entity.Lesson;
import com.nikafom.englishAssistant.model.db.entity.Student;
import com.nikafom.englishAssistant.model.db.repository.LessonRepository;
import com.nikafom.englishAssistant.model.dto.request.HomeworkToLessonRequest;
import com.nikafom.englishAssistant.model.dto.request.LessonInfoRequest;
import com.nikafom.englishAssistant.model.dto.request.LessonToStudentRequest;
import com.nikafom.englishAssistant.model.dto.response.HomeworkInfoResponse;
import com.nikafom.englishAssistant.model.dto.response.LessonInfoResponse;
import com.nikafom.englishAssistant.model.enums.LessonStatus;
import com.nikafom.englishAssistant.utils.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LessonService {
    private final ObjectMapper mapper;
    private final LessonRepository lessonRepository;
    private final StudentService studentService;
    private final HomeworkService homeworkService;

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
        return lessonRepository.findById(id)
                .orElseThrow(() -> new CustomException("Lesson not found", HttpStatus.NOT_FOUND));
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

    public Page<LessonInfoResponse> getAllLessons(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);

        Page<Lesson> allLessons;
        if(filter == null) {
            allLessons = lessonRepository.findAll(pageRequest);
        } else {
            allLessons = lessonRepository.findAllPageableFiltered(pageRequest, filter.toUpperCase());
        }

        List<LessonInfoResponse> content = allLessons.getContent().stream()
                .map(lesson -> mapper.convertValue(lesson, LessonInfoResponse.class))
                .collect(Collectors.toList());
        return new PageImpl<>(content, pageRequest, allLessons.getTotalElements());
    }

    public void addLessonToStudent(LessonToStudentRequest request) {
        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new CustomException("Lesson not found", HttpStatus.NOT_FOUND));

        Student student = studentService.getStudentFromDB(request.getStudentId());

        lesson.setStudent(student);
        lessonRepository.save(lesson);
    }

    public List<LessonInfoResponse> getStudentLessons(Long id) {
        Student student = studentService.getStudentFromDB(id);

        return lessonRepository.findAllByStudentId(student.getId()).stream()
                .map(lesson -> mapper.convertValue(lesson, LessonInfoResponse.class))
                .collect(Collectors.toList());
    }

    public void addHomeworkToLesson(HomeworkToLessonRequest request) {
        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new CustomException("Lesson not found", HttpStatus.NOT_FOUND));

        Homework homework = homeworkService.getHomeworkFromDB(request.getHomeworkId());

        lesson.setHomework(homework);
        lessonRepository.save(lesson);
    }

    public HomeworkInfoResponse getLessonHomework(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new CustomException("Lesson not found", HttpStatus.NOT_FOUND));

        return mapper.convertValue(lesson.getHomework(), HomeworkInfoResponse.class);
    }

    public void markGivenLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new CustomException("Lesson not found", HttpStatus.NOT_FOUND));
        lesson.setStatus(LessonStatus.GIVEN);
        lesson.setUpdatedAt(LocalDateTime.now());
        lessonRepository.save(lesson);
    }

    public void markPaidLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new CustomException("Lesson not found", HttpStatus.NOT_FOUND));
        lesson.setStatus(LessonStatus.PAID);
        lesson.setUpdatedAt(LocalDateTime.now());
        lessonRepository.save(lesson);
    }
}
