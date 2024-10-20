package com.nikafom.englishAssistant.controllers;

import com.nikafom.englishAssistant.model.dto.request.PaymentInfoRequest;
import com.nikafom.englishAssistant.model.dto.request.PaymentToStudentRequest;
import com.nikafom.englishAssistant.model.dto.response.PaymentInfoResponse;
import com.nikafom.englishAssistant.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Оплаты")
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @Operation(summary = "Создать оплату")
    public PaymentInfoResponse createPayment(@RequestBody PaymentInfoRequest request) {
        return paymentService.createPayment(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить оплату по ID")
    public PaymentInfoResponse getPayment(@PathVariable Long id) {
        return paymentService.getPayment(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить оплату по ID")
    public PaymentInfoResponse updatePayment(@PathVariable Long id, @RequestBody PaymentInfoRequest request) {
        return paymentService.updatePayment(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить оплату по ID")
    public void deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить список оплат")
    public Page<PaymentInfoResponse> getAllPayments(@RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer perPage,
                                                    @RequestParam(defaultValue = "lessonPrice") String sort,
                                                    @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                                    @RequestParam(required = false) String filter
    ) {
        return paymentService.getAllPayments(page, perPage, sort, order, filter);
    }

    @PostMapping("/paymentToStudent")
    @Operation(summary = "Добавить оплату ученику")
    public void addPaymentToStudent(@RequestBody PaymentToStudentRequest request) {
        paymentService.addPaymentToStudent(request);
    }

    @GetMapping("/studentPayments/{id}")
    @Operation(summary = "Получить список оплат ученика по ID")
    public List<PaymentInfoResponse> getStudentPayments(@PathVariable Long id) {
        return paymentService.getStudentPayments(id);
    }
}
