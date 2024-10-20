package com.nikafom.englishAssistant.model.db.repository;

import com.nikafom.englishAssistant.model.db.entity.Homework;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework,Long> {
    List<Homework> findAllByStudentId(Long studentId);

    @Query("select h from Homework h where upper(cast(h.status as string)) like %:filter% or upper(h.task) like %:filter% or cast(h.date as string) like %:filter%")
    Page<Homework> findAllPageableFiltered(Pageable request, @Param("filter") String filter);
}
