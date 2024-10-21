package com.nikafom.englishAssistant.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nikafom.englishAssistant.model.dto.request.LessonInfoRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LessonInfoResponse extends LessonInfoRequest {
    Long id;
}
