package com.nikafom.englishAssistant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikafom.englishAssistant.model.db.entity.Payment;
import com.nikafom.englishAssistant.model.db.entity.Student;
import com.nikafom.englishAssistant.model.db.repository.PaymentRepository;
import com.nikafom.englishAssistant.model.dto.request.PaymentInfoRequest;
import com.nikafom.englishAssistant.model.dto.request.PaymentToStudentRequest;
import com.nikafom.englishAssistant.model.dto.response.PaymentInfoResponse;
import com.nikafom.englishAssistant.model.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final ObjectMapper mapper;
    private final PaymentRepository paymentRepository;
    private final StudentService studentService;

    public PaymentInfoResponse createPayment(PaymentInfoRequest request) {
        Payment payment = mapper.convertValue(request, Payment.class);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setStatus(PaymentStatus.CREATED);

        Payment savedPayment = paymentRepository.save(payment);

        return mapper.convertValue(savedPayment, PaymentInfoResponse.class);
    }

    public PaymentInfoResponse getPayment(Long id) {
        Payment payment = getPaymentFromDB(id);
        return mapper.convertValue(payment, PaymentInfoResponse.class);
    }

    public Payment getPaymentFromDB(Long id) {
        return paymentRepository.findById(id).orElse(new Payment());
    }

    public PaymentInfoResponse updatePayment(Long id, PaymentInfoRequest request) {
        Payment payment = getPaymentFromDB(id);

        payment.setLessonPrice(request.getLessonPrice() == null ? payment.getLessonPrice() : request.getLessonPrice());
        payment.setPeriod(request.getPeriod() == null ? payment.getPeriod() : request.getPeriod());
        payment.setDetails(request.getDetails() == null ? payment.getDetails() : request.getDetails());

        payment.setUpdatedAt(LocalDateTime.now());
        payment.setStatus(PaymentStatus.UPDATED);

        Payment savedPayment = paymentRepository.save(payment);

        return mapper.convertValue(savedPayment, PaymentInfoResponse.class);
    }

    public void deletePayment(Long id) {
        Payment payment = getPaymentFromDB(id);
        payment.setUpdatedAt(LocalDateTime.now());
        payment.setStatus(PaymentStatus.DELETED);
        paymentRepository.save(payment);
    }

    public List<PaymentInfoResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(payment -> mapper.convertValue(payment, PaymentInfoResponse.class))
                .collect(Collectors.toList());
    }

    public void addPaymentToStudent(PaymentToStudentRequest request) {
        Payment payment = paymentRepository.findById(request.getPaymentId()).orElse(null);
        if(payment == null) {
            return;
        }

        Student student = studentService.getStudentFromDB(request.getStudentId());
        if(student == null) {
            return;
        }

        payment.setStudent(student);
        paymentRepository.save(payment);
    }

    public List<PaymentInfoResponse> getStudentPayments(Long id) {
        return paymentRepository.findAllByStudentId(id).stream()
                .map(payment -> mapper.convertValue(payment, PaymentInfoResponse.class))
                .collect(Collectors.toList());
    }
}
