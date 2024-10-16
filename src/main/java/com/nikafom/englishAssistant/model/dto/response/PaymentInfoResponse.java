package com.nikafom.englishAssistant.model.dto.response;

import com.nikafom.englishAssistant.model.dto.request.PaymentInfoRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentInfoResponse extends PaymentInfoRequest {
    Long id;
}
