package com.nikafom.englishAssistant.model.db.repository;

import com.nikafom.englishAssistant.model.db.entity.Student;
import com.nikafom.englishAssistant.model.enums.StudentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByPhoneNumber(String phoneNumber);

    Optional<Student> findByEmailIgnoreCase(String email);

    List<Student> findAllByUserId(Long userId);

    Page<Student> findAllByStatusNot(Pageable request, StudentStatus status);

    @Query("select s from Student s where s.status <> :status and (upper(s.name) like %:filter% or upper(s.email) like %:filter% or upper(s.interests) like %:filter% or s.phoneNumber like %:filter%)")
    Page<Student> findAllByStatusNotFiltered(Pageable request, StudentStatus status, @Param("filter") String filter);
}
