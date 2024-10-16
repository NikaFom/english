package com.nikafom.englishAssistant.service;

import com.nikafom.englishAssistant.model.dto.request.GoalInfoRequest;
import com.nikafom.englishAssistant.model.dto.response.GoalInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoalService {
    public GoalInfoResponse createGoal(GoalInfoRequest request) {
        return GoalInfoResponse.builder()
                .description(request.getDescription())
                .type(request.getType())
                .date(request.getDate())
                .status(request.getStatus())
                .build();
    }

    public GoalInfoResponse getGoal(Long id) {
        return null;
    }

    public GoalInfoResponse updateGoal(Long id, GoalInfoRequest request) {
        return GoalInfoResponse.builder()
                .description(request.getDescription())
                .type(request.getType())
                .date(request.getDate())
                .status(request.getStatus())
                .build();
    }

    public void deleteGoal(Long id) {
    }

    public List<GoalInfoResponse> getAllGoals() {
        return Collections.emptyList();
    }
}
