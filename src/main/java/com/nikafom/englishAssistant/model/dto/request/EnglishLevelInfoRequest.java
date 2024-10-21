package com.nikafom.englishAssistant.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nikafom.englishAssistant.model.enums.Level;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnglishLevelInfoRequest {
    @NotEmpty
    String englishLevel;
    Level level;
    String grammar;
    String vocabulary;
    String reading;
    String listening;
    String writing;
    String speaking;
}
