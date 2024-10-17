package com.nikafom.englishAssistant.model.db.repository;

import com.nikafom.englishAssistant.model.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
