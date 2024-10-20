package com.nikafom.englishAssistant.model.db.repository;

import com.nikafom.englishAssistant.model.db.entity.EnglishLevel;
import com.nikafom.englishAssistant.model.enums.EnglishLevelStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnglishLevelRepository extends JpaRepository<EnglishLevel, Long> {
    List<EnglishLevel> findAllByStudentId(Long studentId);

    Page<EnglishLevel> findAllByStatusNot(Pageable request, EnglishLevelStatus status);

    @Query("select el from EnglishLevel el where el.status <> :status and (upper(cast(el.level as string)) like %:filter% or upper(el.englishLevel) like %:filter%)")
    Page<EnglishLevel> findAllByStatusNotFiltered(Pageable request, EnglishLevelStatus status, @Param("filter") String filter);
}
