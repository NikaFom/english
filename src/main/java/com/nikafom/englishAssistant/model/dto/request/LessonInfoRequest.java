package com.nikafom.englishAssistant.model.dto.request;

import com.nikafom.englishAssistant.model.enums.LessonStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonInfoRequest {
    LocalDate date;
    LocalTime time;
    Integer lessonDuration;
}
