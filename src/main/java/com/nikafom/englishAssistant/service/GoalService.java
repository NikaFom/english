package com.nikafom.englishAssistant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikafom.englishAssistant.exceptions.CustomException;
import com.nikafom.englishAssistant.model.db.entity.Goal;
import com.nikafom.englishAssistant.model.db.entity.Student;
import com.nikafom.englishAssistant.model.db.repository.GoalRepository;
import com.nikafom.englishAssistant.model.dto.request.GoalInfoRequest;
import com.nikafom.englishAssistant.model.dto.request.GoalToStudentRequest;
import com.nikafom.englishAssistant.model.dto.response.GoalInfoResponse;
import com.nikafom.englishAssistant.model.enums.GoalStatus;
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
public class GoalService {
    private final ObjectMapper mapper;
    private final GoalRepository goalRepository;
    private final StudentService studentService;

    public GoalInfoResponse createGoal(GoalInfoRequest request) {
        Goal goal = mapper.convertValue(request, Goal.class);
        goal.setCreatedAt(LocalDateTime.now());
        goal.setStatus(GoalStatus.SET);

        Goal savedGoal = goalRepository.save(goal);

        return mapper.convertValue(savedGoal, GoalInfoResponse.class);
    }

    public GoalInfoResponse getGoal(Long id) {
        Goal goal = getGoalFromDB(id);
        return mapper.convertValue(goal, GoalInfoResponse.class);
    }

    public Goal getGoalFromDB(Long id) {
        return goalRepository.findById(id)
                .orElseThrow(() -> new CustomException("Goal not found", HttpStatus.NOT_FOUND));
    }

    public GoalInfoResponse updateGoal(Long id, GoalInfoRequest request) {
        Goal goal = getGoalFromDB(id);

        goal.setDescription(request.getDescription());
        goal.setType(request.getType() == null ? goal.getType() : request.getType());
        goal.setDate(request.getDate() == null ? goal.getDate() : request.getDate());

        goal.setUpdatedAt(LocalDateTime.now());
        goal.setStatus(GoalStatus.UPDATED);

        Goal savedGoal = goalRepository.save(goal);

        return mapper.convertValue(savedGoal, GoalInfoResponse.class);
    }

    public void deleteGoal(Long id) {
        Goal goal = getGoalFromDB(id);
        goal.setUpdatedAt(LocalDateTime.now());
        goal.setStatus(GoalStatus.ABANDONED);
        goalRepository.save(goal);
    }

    public Page<GoalInfoResponse> getAllGoals(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);

        Page<Goal> allGoals;
        if(filter == null) {
            allGoals = goalRepository.findAll(pageRequest);
        } else {
            allGoals = goalRepository.findAllPageableFiltered(pageRequest, filter.toUpperCase());
        }

        List<GoalInfoResponse> content = allGoals.getContent().stream()
                .map(goal -> mapper.convertValue(goal, GoalInfoResponse.class))
                .collect(Collectors.toList());
        return new PageImpl<>(content, pageRequest, allGoals.getTotalElements());
    }

    public void addGoalToStudent(GoalToStudentRequest request) {
        Goal goal = goalRepository.findById(request.getGoalId())
                .orElseThrow(() -> new CustomException("Goal not found", HttpStatus.NOT_FOUND));

        Student student = studentService.getStudentFromDB(request.getStudentId());

        goal.setStudent(student);
        goalRepository.save(goal);
    }

    public List<GoalInfoResponse> getStudentGoals(Long id) {
        Student student = studentService.getStudentFromDB(id);

        return goalRepository.findAllByStudentId(student.getId()).stream()
                .map(goal -> mapper.convertValue(goal, GoalInfoResponse.class))
                .collect(Collectors.toList());
    }

    public void markAccomplishedGoal(Long id) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new CustomException("Goal not found", HttpStatus.NOT_FOUND));
        goal.setStatus(GoalStatus.ACCOMPLISHED);
        goal.setUpdatedAt(LocalDateTime.now());
        goalRepository.save(goal);
    }
}
