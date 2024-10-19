package com.nikafom.englishAssistant.model.db.repository;

import com.nikafom.englishAssistant.model.db.entity.EnglishLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnglishLevelRepository extends JpaRepository<EnglishLevel, Long> {
    List<EnglishLevel> findAllByStudentId(Long studentId);
}
