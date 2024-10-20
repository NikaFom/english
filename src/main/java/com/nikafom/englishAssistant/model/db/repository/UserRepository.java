package com.nikafom.englishAssistant.model.db.repository;

import com.nikafom.englishAssistant.model.db.entity.User;
import com.nikafom.englishAssistant.model.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByEmailIgnoreCase(String email);

    Page<User> findAllByStatusNot(Pageable request, UserStatus status);

    @Query("select u from User u where u.status <> :status and (upper(u.name) like %:filter% or upper(u.email) like %:filter% or u.phoneNumber like %:filter%)")
    Page<User> findAllByStatusNotFiltered(Pageable request, UserStatus status, @Param("filter") String filter);
}
