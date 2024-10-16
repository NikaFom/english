package com.nikafom.englishAssistant.model.db.entity;

import com.nikafom.englishAssistant.model.enums.EnglishLevelStatus;
import com.nikafom.englishAssistant.model.enums.Level;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "english_levels")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnglishLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "english_level")
    String englishLevel;

    @Column(name = "level")
    @Enumerated(EnumType.STRING)
    Level level;

    @Column(name = "grammar")
    String grammar;

    @Column(name = "vocabulary")
    String vocabulary;

    @Column(name = "reading")
    String reading;

    @Column(name = "listening")
    String listening;

    @Column(name = "writing")
    String writing;

    @Column(name = "speaking")
    String speaking;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    EnglishLevelStatus status;
}
