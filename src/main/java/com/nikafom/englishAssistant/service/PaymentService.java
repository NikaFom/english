package com.nikafom.englishAssistant.service;

import com.nikafom.englishAssistant.model.dto.request.PaymentInfoRequest;
import com.nikafom.englishAssistant.model.dto.response.PaymentInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    public PaymentInfoResponse createPayment(PaymentInfoRequest request) {
        return PaymentInfoResponse.builder()
                .lessonPrice(request.getLessonPrice())
                .period(request.getPeriod())
                .details(request.getDetails())
                .build();
    }

    public PaymentInfoResponse getPayment(Long id) {
        return null;
    }

    public PaymentInfoResponse updatePayment(Long id, PaymentInfoRequest request) {
        return PaymentInfoResponse.builder()
                .lessonPrice(request.getLessonPrice())
                .period(request.getPeriod())
                .details(request.getDetails())
                .build();
    }

    public void deletePayment(Long id) {
    }

    public List<PaymentInfoResponse> getAllPayments() {
        return Collections.emptyList();
    }
}
