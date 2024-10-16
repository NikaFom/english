package com.nikafom.englishAssistant.model.dto.response;

import com.nikafom.englishAssistant.model.dto.request.HomeworkInfoRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HomeworkInfoResponse extends HomeworkInfoRequest {
    Long id;
}
