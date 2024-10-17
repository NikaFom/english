package com.nikafom.englishAssistant.model.db.repository;

import com.nikafom.englishAssistant.model.db.entity.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {
}
