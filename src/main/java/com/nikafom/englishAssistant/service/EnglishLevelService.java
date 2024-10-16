package com.nikafom.englishAssistant.service;

import com.nikafom.englishAssistant.model.dto.request.EnglishLevelInfoRequest;
import com.nikafom.englishAssistant.model.dto.response.EnglishLevelInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnglishLevelService {
    public EnglishLevelInfoResponse createEnglishLevel(EnglishLevelInfoRequest request) {
        return EnglishLevelInfoResponse.builder()
                .englishLevel(request.getEnglishLevel())
                .level(request.getLevel())
                .grammar(request.getGrammar())
                .vocabulary(request.getVocabulary())
                .reading(request.getReading())
                .listening(request.getListening())
                .writing(request.getWriting())
                .speaking(request.getSpeaking())
                .build();
    }

    public EnglishLevelInfoResponse getEnglishLevel(Long id) {
        return null;
    }

    public EnglishLevelInfoResponse updateEnglishLevel(Long id, EnglishLevelInfoRequest request) {
        return EnglishLevelInfoResponse.builder()
                .englishLevel(request.getEnglishLevel())
                .level(request.getLevel())
                .grammar(request.getGrammar())
                .vocabulary(request.getVocabulary())
                .reading(request.getReading())
                .listening(request.getListening())
                .writing(request.getWriting())
                .speaking(request.getSpeaking())
                .build();
    }

    public void deleteEnglishLevel(Long id) {
    }

    public List<EnglishLevelInfoResponse> getAllEnglishLevels() {
        return Collections.emptyList();
    }
}
