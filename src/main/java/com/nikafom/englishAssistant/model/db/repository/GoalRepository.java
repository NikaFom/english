package com.nikafom.englishAssistant.model.db.repository;

import com.nikafom.englishAssistant.model.db.entity.Goal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal,Long> {
    List<Goal> findAllByStudentId(Long studentId);

    @Query("select g from Goal g where upper(cast(g.status as string)) like %:filter% or upper(g.description) like %:filter% or upper(g.type) like %:filter% or cast(g.date as string) like %:filter%")
    Page<Goal> finaAllPageableFiltered(Pageable request, @Param("filter") String filter);
}
