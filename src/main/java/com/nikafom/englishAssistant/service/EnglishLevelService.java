package com.nikafom.englishAssistant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikafom.englishAssistant.model.db.entity.EnglishLevel;
import com.nikafom.englishAssistant.model.db.repository.EnglishLevelRepository;
import com.nikafom.englishAssistant.model.dto.request.EnglishLevelInfoRequest;
import com.nikafom.englishAssistant.model.dto.response.EnglishLevelInfoResponse;
import com.nikafom.englishAssistant.model.enums.EnglishLevelStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnglishLevelService {
    private final ObjectMapper mapper;
    private final EnglishLevelRepository englishLevelRepository;

    public EnglishLevelInfoResponse createEnglishLevel(EnglishLevelInfoRequest request) {
        EnglishLevel englishLevel = mapper.convertValue(request, EnglishLevel.class);
        englishLevel.setCreatedAt(LocalDateTime.now());
        englishLevel.setStatus(EnglishLevelStatus.CREATED);

        EnglishLevel savedLevel = englishLevelRepository.save(englishLevel);

        return mapper.convertValue(savedLevel, EnglishLevelInfoResponse.class);
    }

    public EnglishLevelInfoResponse getEnglishLevel(Long id) {
        EnglishLevel englishLevel = getEnglishLevelFromDB(id);
        return mapper.convertValue(englishLevel, EnglishLevelInfoResponse.class);
    }

    public EnglishLevel getEnglishLevelFromDB(Long id) {
        return englishLevelRepository.findById(id).orElse(new EnglishLevel());
    }

    public EnglishLevelInfoResponse updateEnglishLevel(Long id, EnglishLevelInfoRequest request) {
        EnglishLevel englishLevel = getEnglishLevelFromDB(id);

        englishLevel.setEnglishLevel(request.getEnglishLevel());
        englishLevel.setLevel(request.getLevel() == null ? englishLevel.getLevel() : request.getLevel());
        englishLevel.setGrammar(request.getGrammar() == null ? englishLevel.getGrammar() : request.getGrammar());
        englishLevel.setVocabulary(request.getVocabulary() == null ? englishLevel.getVocabulary() : request.getVocabulary());
        englishLevel.setReading(request.getReading() == null ? englishLevel.getReading() : request.getReading());
        englishLevel.setListening(request.getListening() == null ? englishLevel.getListening() : request.getListening());
        englishLevel.setWriting(request.getWriting() == null ? englishLevel.getWriting() : request.getWriting());
        englishLevel.setSpeaking(request.getSpeaking() == null ? englishLevel.getSpeaking() : request.getSpeaking());

        englishLevel.setUpdatedAt(LocalDateTime.now());
        englishLevel.setStatus(EnglishLevelStatus.UPDATED);

        EnglishLevel savedLevel = englishLevelRepository.save(englishLevel);

        return mapper.convertValue(savedLevel, EnglishLevelInfoResponse.class);
    }

    public void deleteEnglishLevel(Long id) {
        EnglishLevel englishLevel = getEnglishLevelFromDB(id);
        englishLevel.setUpdatedAt(LocalDateTime.now());
        englishLevel.setStatus(EnglishLevelStatus.DELETED);
        englishLevelRepository.save(englishLevel);
    }

    public List<EnglishLevelInfoResponse> getAllEnglishLevels() {
        return englishLevelRepository.findAll().stream()
                .map(englishLevel -> mapper.convertValue(englishLevel, EnglishLevelInfoResponse.class))
                .collect(Collectors.toList());
    }
}
