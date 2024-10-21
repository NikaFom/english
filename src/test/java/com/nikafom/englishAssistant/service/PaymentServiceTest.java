package com.nikafom.englishAssistant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikafom.englishAssistant.model.db.entity.Payment;
import com.nikafom.englishAssistant.model.db.entity.Student;
import com.nikafom.englishAssistant.model.db.repository.PaymentRepository;
import com.nikafom.englishAssistant.model.dto.request.PaymentInfoRequest;
import com.nikafom.englishAssistant.model.dto.request.PaymentToStudentRequest;
import com.nikafom.englishAssistant.model.dto.response.PaymentInfoResponse;
import com.nikafom.englishAssistant.model.enums.PaymentStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private StudentService studentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Spy
    private ObjectMapper mapper;

    @Test
    public void createPayment() {
        PaymentInfoRequest request = new PaymentInfoRequest();

        Payment payment = new Payment();
        payment.setId(1L);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentInfoResponse result = paymentService.createPayment(request);

        assertEquals(payment.getId(), result.getId());
    }

    @Test
    public void getPayment() {
        Payment payment = new Payment();
        payment.setId(1L);

        when(paymentRepository.findById(payment.getId())).thenReturn(Optional.of(payment));

        PaymentInfoResponse result = paymentService.getPayment(payment.getId());

        assertEquals(payment.getId(), result.getId());
    }

    @Test
    public void updatePayment() {
        PaymentInfoRequest request = new PaymentInfoRequest();

        Payment payment = new Payment();
        payment.setId(1L);

        when(paymentRepository.findById(payment.getId())).thenReturn(Optional.of(payment));

        paymentService.updatePayment(payment.getId(), request);

        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertEquals(PaymentStatus.UPDATED, payment.getStatus());
    }

    @Test
    public void deletePayment() {
        Payment payment = new Payment();
        payment.setId(1L);

        when(paymentRepository.findById(payment.getId())).thenReturn(Optional.of(payment));

        paymentService.deletePayment(payment.getId());

        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertEquals(PaymentStatus.DELETED, payment.getStatus());
    }

    @Test
    public void getAllPayments() {
        Payment payment1 = new Payment();
        payment1.setLessonPrice(2500);
        payment1.setId(1L);

        Payment payment2 = new Payment();
        payment2.setId(2L);

        List<Payment> payments = List.of(payment1, payment2);
        Page<Payment> pagedPayments = new PageImpl<>(payments);

        Pageable pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, payment1.getLessonPrice().toString());

        when(paymentRepository.findAllByStatusNot(pageRequest, PaymentStatus.DELETED))
                .thenReturn(pagedPayments);

        Page<PaymentInfoResponse> result = paymentService
                .getAllPayments(0, 10, payment1.getLessonPrice().toString(), Sort.Direction.ASC, null);

        assertEquals(pagedPayments.getTotalElements(), result.getTotalElements());
    }

    @Test
    public void getAllPayments_Filtered() {
        Payment payment1 = new Payment();
        payment1.setLessonPrice(2500);
        payment1.setId(1L);

        Payment payment2 = new Payment();
        payment2.setId(2L);

        List<Payment> payments = List.of(payment1, payment2);
        Page<Payment> pagedPayments = new PageImpl<>(payments);

        Pageable pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, payment1.getLessonPrice().toString());

        when(paymentRepository.findAllByStatusNotFiltered(pageRequest, PaymentStatus.DELETED, "A"))
                .thenReturn(pagedPayments);

        Page<PaymentInfoResponse> result = paymentService
                .getAllPayments(0, 10, payment1.getLessonPrice().toString(), Sort.Direction.ASC, "A");

        assertEquals(pagedPayments.getTotalElements(), result.getTotalElements());
    }

    @Test
    public void addPaymentToStudent() {
        Payment payment = new Payment();
        payment.setId(1L);

        when(paymentRepository.findById(payment.getId())).thenReturn(Optional.of(payment));

        Student student = new Student();
        student.setId(1L);

        when(studentService.getStudentFromDB(student.getId())).thenReturn(student);

        PaymentToStudentRequest request = new PaymentToStudentRequest();
        request.setStudentId(student.getId());
        request.setPaymentId(payment.getId());

        paymentService.addPaymentToStudent(request);

        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertEquals(student.getId(), payment.getStudent().getId());
    }

    @Test
    public void getStudentPayments() {
        Student student = new Student();
        student.setId(1L);

        when(studentService.getStudentFromDB(student.getId())).thenReturn(student);

        List<Payment> payments = new ArrayList<>();

        when(paymentRepository.findAllByStudentId(student.getId())).thenReturn(payments);

        List<PaymentInfoResponse> result = paymentService.getStudentPayments(student.getId());

        assertEquals(payments.size(), result.size());
    }
}