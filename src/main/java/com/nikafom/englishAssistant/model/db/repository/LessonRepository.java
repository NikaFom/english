package com.nikafom.englishAssistant.model.db.repository;

import com.nikafom.englishAssistant.model.db.entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findAllByStudentId(Long studentId);

    @Query("select l from Lesson l where upper(cast(l.status as string)) like %:filter% or cast(l.date as string) like %:filter% or cast(l.time as string) like %:filter% or cast(l.lessonDuration as string) like %:filter%")
    Page<Lesson> findAllPageableFiltered(Pageable request, @Param("filter") String filter);
}
