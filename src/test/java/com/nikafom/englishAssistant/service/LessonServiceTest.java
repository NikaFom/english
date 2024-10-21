package com.nikafom.englishAssistant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LessonServiceTest {

    @InjectMocks
    private LessonService lessonService;

    @Mock
    private StudentService studentService;

    @Mock
    private HomeworkService homeworkService;

    @Mock
    private LessonRepository lessonRepository;

    @Spy
    private ObjectMapper mapper;

    @Test
    public void createLesson() {
        LessonInfoRequest request = new LessonInfoRequest();

        Lesson lesson = new Lesson();
        lesson.setId(1L);

        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        LessonInfoResponse result = lessonService.createLesson(request);

        assertEquals(lesson.getId(), result.getId());
    }

    @Test
    public void getLesson() {
        Lesson lesson = new Lesson();
        lesson.setId(1L);

        when(lessonRepository.findById(lesson.getId())).thenReturn(Optional.of(lesson));

        LessonInfoResponse result = lessonService.getLesson(lesson.getId());

        assertEquals(lesson.getId(), result.getId());
    }

    @Test
    public void updateLesson() {
        LessonInfoRequest request = new LessonInfoRequest();

        Lesson lesson = new Lesson();
        lesson.setId(1L);

        when(lessonRepository.findById(lesson.getId())).thenReturn(Optional.of(lesson));

        lessonService.updateLesson(lesson.getId(), request);

        verify(lessonRepository, times(1)).save(any(Lesson.class));
        assertEquals(LessonStatus.RESCHEDULED, lesson.getStatus());
    }

    @Test
    public void deleteLesson() {
        Lesson lesson = new Lesson();
        lesson.setId(1L);

        when(lessonRepository.findById(lesson.getId())).thenReturn(Optional.of(lesson));

        lessonService.deleteLesson(lesson.getId());

        verify(lessonRepository, times(1)).save(any(Lesson.class));
        assertEquals(LessonStatus.CANCELLED, lesson.getStatus());
    }

    @Test
    public void getAllLessons() {
        Lesson lesson1 = new Lesson();
        lesson1.setLessonDuration(60);
        lesson1.setId(1L);

        Lesson lesson2 = new Lesson();
        lesson2.setId(2L);

        List<Lesson> lessons = List.of(lesson1, lesson2);
        Page<Lesson> pagedLessons = new PageImpl<>(lessons);

        Pageable pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, lesson1.getLessonDuration().toString());

        when(lessonRepository.findAll(pageRequest))
                .thenReturn(pagedLessons);

        Page<LessonInfoResponse> result = lessonService
                .getAllLessons(0, 10, lesson1.getLessonDuration().toString(), Sort.Direction.ASC, null);

        assertEquals(pagedLessons.getTotalElements(), result.getTotalElements());
    }

    @Test
    public void getAllLessons_Filtered() {
        Lesson lesson1 = new Lesson();
        lesson1.setLessonDuration(60);
        lesson1.setId(1L);

        Lesson lesson2 = new Lesson();
        lesson2.setId(2L);

        List<Lesson> lessons = List.of(lesson1, lesson2);
        Page<Lesson> pagedLessons = new PageImpl<>(lessons);

        Pageable pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, lesson1.getLessonDuration().toString());

        when(lessonRepository.findAllPageableFiltered(pageRequest, "A"))
                .thenReturn(pagedLessons);

        Page<LessonInfoResponse> result = lessonService
                .getAllLessons(0, 10, lesson1.getLessonDuration().toString(), Sort.Direction.ASC, "A");

        assertEquals(pagedLessons.getTotalElements(), result.getTotalElements());
    }

    @Test
    public void addLessonToStudent() {
        Lesson lesson = new Lesson();
        lesson.setId(1L);

        when(lessonRepository.findById(lesson.getId())).thenReturn(Optional.of(lesson));

        Student student = new Student();
        student.setId(1L);

        when((studentService.getStudentFromDB(student.getId()))).thenReturn(student);

        LessonToStudentRequest request = new LessonToStudentRequest();
        request.setStudentId(student.getId());
        request.setLessonId(lesson.getId());

        lessonService.addLessonToStudent(request);

        verify(lessonRepository, times(1)).save(any(Lesson.class));
        assertEquals(student.getId(), lesson.getStudent().getId());
    }

    @Test
    public void getStudentLessons() {
        Student student = new Student();
        student.setId(1L);

        when(studentService.getStudentFromDB(student.getId())).thenReturn(student);

        List<Lesson> lessons = new ArrayList<>();

        when(lessonRepository.findAllByStudentId(student.getId())).thenReturn(lessons);

        List<LessonInfoResponse> result = lessonService.getStudentLessons(student.getId());

        assertEquals(lessons.size(), result.size());
    }

    @Test
    public void addHomeworkToLesson() {
        Lesson lesson = new Lesson();
        lesson.setId(1L);

        when(lessonRepository.findById(lesson.getId())).thenReturn(Optional.of(lesson));

        Homework homework = new Homework();
        homework.setId(1L);

        when(homeworkService.getHomeworkFromDB(homework.getId())).thenReturn(homework);

        HomeworkToLessonRequest request = new HomeworkToLessonRequest();
        request.setLessonId(lesson.getId());
        request.setHomeworkId(homework.getId());

        lessonService.addHomeworkToLesson(request);

        verify(lessonRepository, times(1)).save(any(Lesson.class));
        assertEquals(homework.getId(), lesson.getHomework().getId());
    }

    @Test
    public void getLessonHomework() {
        Lesson lesson = new Lesson();
        lesson.setId(1L);

        when(lessonRepository.findById(lesson.getId())).thenReturn(Optional.of(lesson));

        HomeworkInfoResponse result = lessonService.getLessonHomework(lesson.getId());

        assertEquals(result, lesson.getHomework());
    }

    @Test
    public void markGivenLesson() {
        Lesson lesson = new Lesson();
        lesson.setId(1L);

        when(lessonRepository.findById(lesson.getId())).thenReturn(Optional.of(lesson));

        lessonService.markGivenLesson(lesson.getId());

        verify(lessonRepository, times(1)).save(any(Lesson.class));
        assertEquals(LessonStatus.GIVEN, lesson.getStatus());
    }

    @Test
    public void markPaidLesson() {
        Lesson lesson = new Lesson();
        lesson.setId(1L);

        when(lessonRepository.findById(lesson.getId())).thenReturn(Optional.of(lesson));

        lessonService.markPaidLesson(lesson.getId());

        verify(lessonRepository, times(1)).save(any(Lesson.class));
        assertEquals(LessonStatus.PAID, lesson.getStatus());
    }
}