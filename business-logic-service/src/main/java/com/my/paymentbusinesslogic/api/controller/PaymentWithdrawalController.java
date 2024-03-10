package com.my.paymentbusinesslogic.api.controller;

import com.my.paymentbusinesslogic.service.PaymentWithdrawalService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/payment-withdrawals")
public class PaymentWithdrawalController {
  private PaymentWithdrawalService paymentWithdrawalService;

  @Operation(summary = "Delete payment withdrawal.")
  @DeleteMapping("/{paymentWithdrawalId}")
  public ResponseEntity<Void> deletePaymentWithdrawal(@PathVariable Long paymentWithdrawalId) {
    paymentWithdrawalService.deletePaymentWithdrawal(paymentWithdrawalId);
    return ResponseEntity.status(HttpStatus.OK)
        .build();
  }
}
