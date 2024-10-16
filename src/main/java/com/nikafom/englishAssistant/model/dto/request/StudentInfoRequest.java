package com.nikafom.englishAssistant.model.dto.request;

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
public class StudentInfoRequest {
    @NotEmpty
    String name;
    @NotEmpty
    String phoneNumber;
    @NotEmpty
    String email;
    String interests;
}
