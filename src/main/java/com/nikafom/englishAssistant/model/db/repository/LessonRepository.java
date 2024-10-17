package com.nikafom.englishAssistant.model.db.repository;

import com.nikafom.englishAssistant.model.db.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
}
