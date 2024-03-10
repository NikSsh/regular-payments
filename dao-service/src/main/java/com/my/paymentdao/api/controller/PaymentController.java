package com.my.paymentdao.api.controller;

import com.my.paymentdao.dto.payment.PaymentBriefResponse;
import com.my.paymentdao.dto.payment.PaymentClientBriefResponse;
import com.my.paymentdao.dto.payment.PaymentRequest;
import com.my.paymentdao.dto.payment.PaymentResponse;
import com.my.paymentdao.dto.paymentwithdrawal.PaymentWithdrawalResponse;
import com.my.paymentdao.service.PaymentService;
import com.my.paymentdao.service.PaymentWithdrawalService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping({"/api/v1/payments"})
public class  PaymentController {
  private final PaymentService paymentService;
  private final PaymentWithdrawalService paymentWithdrawalService;

  @PostMapping
  public ResponseEntity<PaymentResponse> createPayment(
      @RequestBody PaymentRequest paymentRequest) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(paymentService.createPayment(paymentRequest));
  }

  @PutMapping({"/{id}"})
  public ResponseEntity<PaymentResponse> updatePayment(
      @PathVariable Long id, @RequestBody PaymentRequest paymentRequest) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(paymentService.updatePayment(id, paymentRequest));
  }

  @GetMapping
  public ResponseEntity<List<PaymentBriefResponse>> getAll() {
    return ResponseEntity.status(HttpStatus.OK).body(paymentService.getPayments());
  }

  @GetMapping({"/inn/{inn}"})
  public ResponseEntity<List<PaymentClientBriefResponse>> getAllByInn(@PathVariable String inn) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(paymentService.getPaymentsByInn(inn));
  }

  @GetMapping({"/okpo/{okpo}"})
  public ResponseEntity<List<PaymentClientBriefResponse>> getAllByOkpo(@PathVariable String okpo) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(paymentService.getPaymentsByOkpo(okpo));
  }

  @GetMapping({"/{id}"})
  public ResponseEntity<PaymentResponse> getById(@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(paymentService.getPaymentById(id));
  }

  @DeleteMapping({"/{id}"})
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    this.paymentService.deletePaymentById(id);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping({"/{id}/withdrawals"})
  public ResponseEntity<PaymentWithdrawalResponse> createPaymentWithdrawal(@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(paymentWithdrawalService.createPaymentWithdrawal(id));
  }

  @GetMapping({"/{id}/withdrawals"})
  public ResponseEntity<List<PaymentWithdrawalResponse>> getPaymentWithdrawals(
      @PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(paymentWithdrawalService.getAllByPaymentId(id));
  }
}
