package com.nikafom.englishAssistant.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nikafom.englishAssistant.model.enums.HomeworkStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HomeworkInfoRequest {
    @NotEmpty
    String task;
    LocalDate date;
    HomeworkStatus status;
}
