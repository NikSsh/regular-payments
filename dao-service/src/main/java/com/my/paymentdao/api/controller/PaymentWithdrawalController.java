package com.my.paymentdao.api.controller;

import com.my.paymentdao.dto.paymentwithdrawal.PaymentWithdrawalResponse;
import com.my.paymentdao.service.PaymentWithdrawalService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping({"/api/v1/payment-withdrawals"})
public class PaymentWithdrawalController {
  private final PaymentWithdrawalService paymentWithdrawalService;

  @GetMapping({"/{paymentWithdrawalId}"})
  public ResponseEntity<PaymentWithdrawalResponse> getPaymentWithdrawal(
      @PathVariable Long paymentWithdrawalId) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(paymentWithdrawalService.getById(paymentWithdrawalId));
  }

  @DeleteMapping({"/{paymentWithdrawalId}"})
  public ResponseEntity<Void> deletePaymentWithdrawal(@PathVariable Long paymentWithdrawalId) {
    paymentWithdrawalService.changePaymentWithdrawalStatusToStornowed(paymentWithdrawalId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
