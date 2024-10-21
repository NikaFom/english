package com.nikafom.englishAssistant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikafom.englishAssistant.model.db.entity.Goal;
import com.nikafom.englishAssistant.model.db.entity.Student;
import com.nikafom.englishAssistant.model.db.repository.GoalRepository;
import com.nikafom.englishAssistant.model.dto.request.GoalInfoRequest;
import com.nikafom.englishAssistant.model.dto.request.GoalToStudentRequest;
import com.nikafom.englishAssistant.model.dto.response.GoalInfoResponse;
import com.nikafom.englishAssistant.model.enums.GoalStatus;
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
public class GoalServiceTest {

    @InjectMocks
    private GoalService goalService;

    @Mock
    private StudentService studentService;

    @Mock
    private GoalRepository goalRepository;

    @Spy
    private ObjectMapper mapper;

    @Test
    public void createGoal() {
        GoalInfoRequest request = new GoalInfoRequest();

        Goal goal = new Goal();
        goal.setId(1L);

        when(goalRepository.save(any(Goal.class))).thenReturn(goal);

        GoalInfoResponse result = goalService.createGoal(request);

        assertEquals(goal.getId(), result.getId());
    }

    @Test
    public void getGoal() {
        Goal goal = new Goal();
        goal.setId(1L);

        when(goalRepository.findById(goal.getId())).thenReturn(Optional.of(goal));

        GoalInfoResponse result = goalService.getGoal(goal.getId());

        assertEquals(goal.getId(), result.getId());
    }

    @Test
    public void updateGoal() {
        GoalInfoRequest request = new GoalInfoRequest();

        Goal goal = new Goal();
        goal.setId(1L);

        when(goalRepository.findById(goal.getId())).thenReturn(Optional.of(goal));

        goalService.updateGoal(goal.getId(), request);

        verify(goalRepository, times(1)).save(any(Goal.class));
        assertEquals(GoalStatus.UPDATED, goal.getStatus());
    }

    @Test
    public void deleteGoal() {
        Goal goal = new Goal();
        goal.setId(1L);

        when(goalRepository.findById(goal.getId())).thenReturn(Optional.of(goal));

        goalService.deleteGoal(goal.getId());

        verify(goalRepository, times(1)).save(any(Goal.class));
        assertEquals(GoalStatus.ABANDONED, goal.getStatus());
    }

    @Test
    public void getAllGoals() {
        Goal goal1 = new Goal();
        goal1.setDescription("exam");
        goal1.setId(1L);

        Goal goal2 = new Goal();
        goal2.setId(2L);

        List<Goal> goals = List.of(goal1, goal2);
        Page<Goal> pagedGoals = new PageImpl<>(goals);

        Pageable pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, goal1.getDescription());

        when(goalRepository.findAll(pageRequest)).thenReturn(pagedGoals);

        Page<GoalInfoResponse> result = goalService
                .getAllGoals(0, 10, goal1.getDescription(), Sort.Direction.ASC, null);

        assertEquals(pagedGoals.getTotalElements(), result.getTotalElements());
    }

    @Test
    public void getAllGoals_Filtered() {
        Goal goal1 = new Goal();
        goal1.setDescription("exam");
        goal1.setId(1L);

        Goal goal2 = new Goal();
        goal2.setId(2L);

        List<Goal> goals = List.of(goal1, goal2);
        Page<Goal> pagedGoals = new PageImpl<>(goals);

        Pageable pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, goal1.getDescription());

        when(goalRepository.findAllPageableFiltered(pageRequest, "A")).thenReturn(pagedGoals);

        Page<GoalInfoResponse> result = goalService
                .getAllGoals(0, 10, goal1.getDescription(), Sort.Direction.ASC, "A");

        assertEquals(pagedGoals.getTotalElements(), result.getTotalElements());
    }

    @Test
    public void addGoalToStudent() {
        Goal goal = new Goal();
        goal.setId(1L);

        when(goalRepository.findById(goal.getId())).thenReturn(Optional.of(goal));

        Student student = new Student();
        student.setId(1L);

        when(studentService.getStudentFromDB(student.getId())).thenReturn(student);

        GoalToStudentRequest request = new GoalToStudentRequest();
        request.setStudentId(student.getId());
        request.setGoalId(goal.getId());

        goalService.addGoalToStudent(request);

        verify(goalRepository, times(1)).save(any(Goal.class));
        assertEquals(student.getId(), goal.getStudent().getId());
    }

    @Test
    public void getStudentGoals() {
        Student student = new Student();
        student.setId(1L);

        when(studentService.getStudentFromDB(student.getId())).thenReturn(student);

        List<Goal> goals = new ArrayList<>();

        when(goalRepository.findAllByStudentId(student.getId())).thenReturn(goals);

        List<GoalInfoResponse> result = goalService.getStudentGoals(student.getId());

        assertEquals(goals.size(), result.size());
    }

    @Test
    public void markAccomplishedGoal() {
        Goal goal = new Goal();
        goal.setId(1L);

        when(goalRepository.findById(goal.getId())).thenReturn(Optional.of(goal));

        goalService.markAccomplishedGoal(goal.getId());

        verify(goalRepository, times(1)).save(any(Goal.class));
        assertEquals(GoalStatus.ACCOMPLISHED, goal.getStatus());
    }
}