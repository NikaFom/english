package com.nikafom.englishAssistant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikafom.englishAssistant.model.db.entity.Goal;
import com.nikafom.englishAssistant.model.db.repository.GoalRepository;
import com.nikafom.englishAssistant.model.dto.request.GoalInfoRequest;
import com.nikafom.englishAssistant.model.dto.response.GoalInfoResponse;
import com.nikafom.englishAssistant.model.enums.GoalStatus;
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
public class GoalService {
    private final ObjectMapper mapper;
    private final GoalRepository goalRepository;

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
        return goalRepository.findById(id).orElse(new Goal());
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

    public List<GoalInfoResponse> getAllGoals() {
        return goalRepository.findAll().stream()
                .map(goal -> mapper.convertValue(goal, GoalInfoResponse.class))
                .collect(Collectors.toList());
    }
}
