package com.nikafom.englishAssistant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikafom.englishAssistant.model.db.entity.Homework;
import com.nikafom.englishAssistant.model.db.entity.Student;
import com.nikafom.englishAssistant.model.db.repository.HomeworkRepository;
import com.nikafom.englishAssistant.model.dto.request.HomeworkInfoRequest;
import com.nikafom.englishAssistant.model.dto.request.HomeworkToStudentRequest;
import com.nikafom.englishAssistant.model.dto.response.HomeworkInfoResponse;
import com.nikafom.englishAssistant.model.enums.HomeworkStatus;
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
public class HomeworkServiceTest {

    @InjectMocks
    private HomeworkService homeworkService;

    @Mock
    private StudentService studentService;

    @Mock
    private HomeworkRepository homeworkRepository;

    @Spy
    private ObjectMapper mapper;

    @Test
    public void createHomework() {
        HomeworkInfoRequest request = new HomeworkInfoRequest();

        Homework homework = new Homework();
        homework.setId(1L);

        when(homeworkRepository.save(any(Homework.class))).thenReturn(homework);

        HomeworkInfoResponse result = homeworkService.createHomework(request);

        assertEquals(homework.getId(), result.getId());
    }

    @Test
    public void getHomework() {
        Homework homework = new Homework();
        homework.setId(1L);

        when(homeworkRepository.findById(homework.getId())).thenReturn(Optional.of(homework));

        HomeworkInfoResponse result = homeworkService.getHomework(homework.getId());

        assertEquals(homework.getId(), result.getId());
    }

    @Test
    public void updateHomework() {
        HomeworkInfoRequest request = new HomeworkInfoRequest();

        Homework homework = new Homework();
        homework.setId(1L);

        when(homeworkRepository.findById(homework.getId())).thenReturn(Optional.of(homework));

        homeworkService.updateHomework(homework.getId(), request);

        verify(homeworkRepository, times(1)).save(any(Homework.class));
        assertEquals(HomeworkStatus.CHANGED, homework.getStatus());
    }

    @Test
    public void deleteHomework() {
        Homework homework = new Homework();
        homework.setId(1L);

        when(homeworkRepository.findById(homework.getId())).thenReturn(Optional.of(homework));

        homeworkService.deleteHomework(homework.getId());

        verify(homeworkRepository, times(1)).save(any(Homework.class));
        assertEquals(HomeworkStatus.DELETED, homework.getStatus());
    }

    @Test
    public void getAllHomework() {
        Homework homework1 = new Homework();
        homework1.setTask("do exercises");
        homework1.setId(1L);

        Homework homework2 = new Homework();
        homework2.setId(2L);

        List<Homework> homework = List.of(homework1, homework2);
        Page<Homework> pagedHomework = new PageImpl<>(homework);

        Pageable pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, homework1.getTask());

        when(homeworkRepository.findAll(pageRequest))
                .thenReturn(pagedHomework);

        Page<HomeworkInfoResponse> result = homeworkService
                .getAllHomework(0, 10, homework1.getTask(), Sort.Direction.ASC, null);

        assertEquals(pagedHomework.getTotalElements(), result.getTotalElements());
    }

    @Test
    public void getAllHomework_Filtered() {
        Homework homework1 = new Homework();
        homework1.setTask("do exercises");
        homework1.setId(1L);

        Homework homework2 = new Homework();
        homework2.setId(2L);

        List<Homework> homework = List.of(homework1, homework2);
        Page<Homework> pagedHomework = new PageImpl<>(homework);

        Pageable pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, homework1.getTask());

        when(homeworkRepository.findAllPageableFiltered(pageRequest, "A"))
                .thenReturn(pagedHomework);

        Page<HomeworkInfoResponse> result = homeworkService
                .getAllHomework(0, 10, homework1.getTask(), Sort.Direction.ASC, "A");

        assertEquals(pagedHomework.getTotalElements(), result.getTotalElements());
    }


    @Test
    public void addHomeworkToStudent() {
        Homework homework = new Homework();
        homework.setId(1L);

        when(homeworkRepository.findById(homework.getId())).thenReturn(Optional.of(homework));

        Student student = new Student();
        student.setId(1L);

        when(studentService.getStudentFromDB(student.getId())).thenReturn(student);

        HomeworkToStudentRequest request = new HomeworkToStudentRequest();
        request.setStudentId(student.getId());
        request.setHomeworkId(homework.getId());

        homeworkService.addHomeworkToStudent(request);

        verify(homeworkRepository, times(1)).save(homework);
        assertEquals(student.getId(), homework.getStudent().getId());
    }

    @Test
    public void getStudentHomework() {
        Student student = new Student();
        student.setId(1L);

        when(studentService.getStudentFromDB(student.getId())).thenReturn(student);

        List<Homework> homework = new ArrayList<>();

        when(homeworkRepository.findAllByStudentId(student.getId())).thenReturn(homework);

        List<HomeworkInfoResponse> result = homeworkService.getStudentHomework(student.getId());

        assertEquals(homework.size(), result.size());
    }

    @Test
    public void markDoneHomework() {
        Homework homework = new Homework();
        homework.setId(1L);

        when(homeworkRepository.findById(homework.getId())).thenReturn(Optional.of(homework));

        homeworkService.markDoneHomework(homework.getId());

        verify(homeworkRepository, times(1)).save(any(Homework.class));
        assertEquals(HomeworkStatus.DONE, homework.getStatus());
    }

    @Test
    public void markNotDoneHomework() {
        Homework homework = new Homework();
        homework.setId(1L);

        when(homeworkRepository.findById(homework.getId())).thenReturn(Optional.of(homework));

        homeworkService.markNotDoneHomework(homework.getId());

        verify(homeworkRepository, times(1)).save(any(Homework.class));
        assertEquals(HomeworkStatus.NOT_DONE, homework.getStatus());
    }
}