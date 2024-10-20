package com.nikafom.englishAssistant.model.db.repository;

import com.nikafom.englishAssistant.model.db.entity.Payment;
import com.nikafom.englishAssistant.model.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByStudentId(Long studentId);

    Page<Payment> findAllByStatusNot(Pageable request, PaymentStatus status);

    @Query("select p from Payment p where p.status <> :status and (upper(p.period) like %:filter% or upper(p.details) like %:filter% or cast(p.lessonPrice as string) like %:filter%)")
    Page<Payment> findAllByStatusNotFiltered(Pageable request, PaymentStatus status, @Param("filter") String filter);
}
