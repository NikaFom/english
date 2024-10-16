package com.nikafom.englishAssistant.model.dto.request;

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
    String yes;
    String no;
    String bla;
}
