package com.my.paymentbusinesslogic.api.controller;

import com.my.paymentbusinesslogic.dto.client.paymentdao.PaymentBriefResponse;
import com.my.paymentbusinesslogic.dto.client.paymentdao.PaymentClientBriefResponse;
import com.my.paymentbusinesslogic.dto.client.paymentdao.PaymentResponse;
import com.my.paymentbusinesslogic.dto.client.paymentdao.PaymentWithdrawalResponse;
import com.my.paymentbusinesslogic.dto.payment.PaymentRequest;
import com.my.paymentbusinesslogic.service.PaymentService;
import com.my.paymentbusinesslogic.service.PaymentWithdrawalService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
  private final PaymentService paymentService;
  private final PaymentWithdrawalService paymentWithdrawalService;

  @Operation(summary = "Create a new payment order.")
  @PostMapping
  ResponseEntity<PaymentResponse> createOrder(@Valid @RequestBody PaymentRequest paymentRequest) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(paymentService.createPayment(paymentRequest));
  }

  @Operation(summary = "Get all payments.")
  @GetMapping
  ResponseEntity<List<PaymentBriefResponse>> getPayments() {
    return ResponseEntity.status(HttpStatus.OK)
        .body(paymentService.getPayments());
  }

  @Operation(summary = "Get all payment withdrawals.")
  @GetMapping("/{paymentId}/withdrawals")
  ResponseEntity<List<PaymentWithdrawalResponse>> getPaymentWithdrawals(
      @PathVariable Long paymentId) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(paymentWithdrawalService.getPaymentWithdrawalsByPaymentId(paymentId));
  }

  @Operation(summary = "Get all payments by payer INN.")
  @GetMapping("/inn/{inn}")
  ResponseEntity<List<PaymentClientBriefResponse>> getPaymentsByInn(@PathVariable String inn) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(paymentService.getPaymentsByInn(inn));
  }

  @Operation(summary = "Get all payments by recipient OKPO.")
  @GetMapping("/okpo/{okpo}")
  ResponseEntity<List<PaymentClientBriefResponse>> getPaymentsByOkpo(@PathVariable String okpo) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(paymentService.getPaymentsByOkpo(okpo));
  }

  @Operation(summary = "Create payment withdrawal.")
  @PostMapping("/{paymentId}/withdrawals")
  ResponseEntity<PaymentWithdrawalResponse> createPaymentWithdrawal(@PathVariable Long paymentId) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(paymentWithdrawalService.createPaymentWithdrawal(paymentId));
  }

  @Operation(summary = "Checks if a new payment withdrawal is needed.")
  @GetMapping("/{paymentId}/withdrawal-needed-check")
  ResponseEntity<Map<String, Boolean>> isPaymentWithdrawalNeeded(@PathVariable Long paymentId) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(Map.of("isWithdrawalNeeded",
            paymentWithdrawalService.isPaymentWithdrawalNeeded(paymentId)));
  }
}
