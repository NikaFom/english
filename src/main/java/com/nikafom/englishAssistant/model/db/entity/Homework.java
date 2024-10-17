package com.nikafom.englishAssistant.model.db.entity;

import com.nikafom.englishAssistant.model.enums.HomeworkStatus;
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
@Table(name = "homework")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Homework {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "task")
    String task;

    @Column(name = "date")
    LocalDate date;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    HomeworkStatus status;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @ManyToOne
    Student student;
}
