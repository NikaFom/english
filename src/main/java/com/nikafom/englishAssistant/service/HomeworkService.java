package com.nikafom.englishAssistant.service;

import com.nikafom.englishAssistant.model.dto.request.HomeworkInfoRequest;
import com.nikafom.englishAssistant.model.dto.response.HomeworkInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeworkService {
    public HomeworkInfoResponse createHomework(HomeworkInfoRequest request) {
        return HomeworkInfoResponse.builder()
                .task(request.getTask())
                .date(request.getDate())
                .status(request.getStatus())
                .build();
    }

    public HomeworkInfoResponse getHomework(Long id) {
        return null;
    }

    public HomeworkInfoResponse updateHomework(Long id, HomeworkInfoRequest request) {
        return HomeworkInfoResponse.builder()
                .task(request.getTask())
                .date(request.getDate())
                .status(request.getStatus())
                .build();
    }

    public void deleteHomework(Long id) {
    }

    public List<HomeworkInfoResponse> getAllHomework() {
        return Collections.emptyList();
    }
}
