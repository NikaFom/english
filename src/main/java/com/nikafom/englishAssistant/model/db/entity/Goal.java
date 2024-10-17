package com.nikafom.englishAssistant.model.db.entity;

import com.nikafom.englishAssistant.model.enums.GoalStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "goals")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "description")
    String description;

    @Column(name = "type")
    String type;

    @Column(name = "date")
    LocalDate date;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    GoalStatus status;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @ManyToOne
    Student student;
}
