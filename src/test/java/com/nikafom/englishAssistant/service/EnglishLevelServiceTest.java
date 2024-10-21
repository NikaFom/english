package com.nikafom.englishAssistant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikafom.englishAssistant.model.db.entity.EnglishLevel;
import com.nikafom.englishAssistant.model.db.entity.Student;
import com.nikafom.englishAssistant.model.db.repository.EnglishLevelRepository;
import com.nikafom.englishAssistant.model.dto.request.EnglishLevelInfoRequest;
import com.nikafom.englishAssistant.model.dto.request.LevelToStudentRequest;
import com.nikafom.englishAssistant.model.dto.response.EnglishLevelInfoResponse;
import com.nikafom.englishAssistant.model.enums.EnglishLevelStatus;
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
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EnglishLevelServiceTest {

    @InjectMocks
    private EnglishLevelService englishLevelService;

    @Mock
    private StudentService studentService;

    @Mock
    private EnglishLevelRepository englishLevelRepository;

    @Spy
    private ObjectMapper mapper;

    @Test
    public void createEnglishLevel() {
        EnglishLevelInfoRequest request = new EnglishLevelInfoRequest();

        EnglishLevel englishLevel = new EnglishLevel();
        englishLevel.setId(1L);

        when(englishLevelRepository.save(any(EnglishLevel.class))).thenReturn(englishLevel);

        EnglishLevelInfoResponse result = englishLevelService.createEnglishLevel(request);

        assertEquals(englishLevel.getId(), result.getId());
    }

    @Test
    public void getEnglishLevel() {
        EnglishLevel englishLevel = new EnglishLevel();
        englishLevel.setId(1L);

        when(englishLevelRepository.findById(englishLevel.getId())).thenReturn(Optional.of(englishLevel));

        EnglishLevelInfoResponse result = englishLevelService.getEnglishLevel(englishLevel.getId());

        assertEquals(englishLevel.getId(), result.getId());
    }

    @Test
    public void updateEnglishLevel() {
        EnglishLevelInfoRequest request = new EnglishLevelInfoRequest();

        EnglishLevel englishLevel = new EnglishLevel();
        englishLevel.setId(1L);

        when(englishLevelRepository.findById(englishLevel.getId())).thenReturn(Optional.of(englishLevel));

        englishLevelService.updateEnglishLevel(englishLevel.getId(), request);

        verify(englishLevelRepository, times(1)).save(any(EnglishLevel.class));
        assertEquals(EnglishLevelStatus.UPDATED, englishLevel.getStatus());
    }

    @Test
    public void deleteEnglishLevel() {
        EnglishLevel englishLevel = new EnglishLevel();
        englishLevel.setId(1L);

        when(englishLevelRepository.findById(englishLevel.getId())).thenReturn(Optional.of(englishLevel));

        englishLevelService.deleteEnglishLevel(englishLevel.getId());

        verify(englishLevelRepository, times(1)).save(any(EnglishLevel.class));
        assertEquals(EnglishLevelStatus.DELETED, englishLevel.getStatus());
    }

    @Test
    public void getAllEnglishLevels() {
        EnglishLevel englishLevel1 = new EnglishLevel();
        englishLevel1.setEnglishLevel("good");
        englishLevel1.setId(1L);

        EnglishLevel englishLevel2 = new EnglishLevel();
        englishLevel2.setId(2L);

        List<EnglishLevel> englishLevels = List.of(englishLevel1, englishLevel2);
        Page<EnglishLevel> pagedEnglishLevels = new PageImpl<>(englishLevels);

        Pageable pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, englishLevel1.getEnglishLevel());

        when(englishLevelRepository.findAllByStatusNot(pageRequest, EnglishLevelStatus.DELETED))
                .thenReturn(pagedEnglishLevels);

        Page<EnglishLevelInfoResponse> result = englishLevelService
                .getAllEnglishLevels(0, 10, englishLevel1.getEnglishLevel(), Sort.Direction.ASC, null);

        assertEquals(pagedEnglishLevels.getTotalElements(), result.getTotalElements());
    }

    @Test
    public void getAllEnglishLevels_Filtered() {
        EnglishLevel englishLevel1 = new EnglishLevel();
        englishLevel1.setEnglishLevel("good");
        englishLevel1.setId(1L);

        EnglishLevel englishLevel2 = new EnglishLevel();
        englishLevel2.setId(2L);

        List<EnglishLevel> englishLevels = List.of(englishLevel1, englishLevel2);
        Page<EnglishLevel> pagedEnglishLevels = new PageImpl<>(englishLevels);

        Pageable pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, englishLevel1.getEnglishLevel());

        when(englishLevelRepository.findAllByStatusNotFiltered(pageRequest, EnglishLevelStatus.DELETED, "A"))
                .thenReturn(pagedEnglishLevels);

        Page<EnglishLevelInfoResponse> result = englishLevelService
                .getAllEnglishLevels(0, 10, englishLevel1.getEnglishLevel(), Sort.Direction.ASC, "A");

        assertEquals(pagedEnglishLevels.getTotalElements(), result.getTotalElements());
    }

    @Test
    public void addEnglishLevelToStudent() {
        EnglishLevel englishLevel = new EnglishLevel();
        englishLevel.setId(1L);

        when(englishLevelRepository.findById(englishLevel.getId()))
                .thenReturn(Optional.of(englishLevel));

        Student student = new Student();
        student.setId(1L);

        when(studentService.getStudentFromDB(student.getId())).thenReturn(student);

        LevelToStudentRequest request = new LevelToStudentRequest();
        request.setStudentId(student.getId());
        request.setEnglishLevelId(englishLevel.getId());

        englishLevelService.addEnglishLevelToStudent(request);

        verify(englishLevelRepository, times(1)).save(any(EnglishLevel.class));
        assertEquals(student.getId(), englishLevel.getStudent().getId());
    }

    @Test
    public void getStudentEnglishLevels() {
        Student student = new Student();
        student.setId(1L);

        when(studentService.getStudentFromDB(student.getId())).thenReturn(student);

        List<EnglishLevel> englishLevels = new ArrayList<>();

        when(englishLevelRepository.findAllByStudentId(student.getId())).thenReturn(englishLevels);

        List<EnglishLevelInfoResponse> result = englishLevelService.getStudentEnglishLevels(student.getId());

        assertEquals(englishLevels.size(), result.size());
    }
}