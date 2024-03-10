package com.my.paymentbusinesslogic.service.client;

import com.my.paymentbusinesslogic.dto.client.paymentdao.PaymentBriefResponse;
import com.my.paymentbusinesslogic.dto.client.paymentdao.PaymentClientBriefResponse;
import com.my.paymentbusinesslogic.dto.client.paymentdao.PaymentResponse;
import com.my.paymentbusinesslogic.dto.client.paymentdao.PaymentWithdrawalResponse;
import com.my.paymentbusinesslogic.dto.payment.PaymentRequest;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign client interface for communicating with the payment dao service.
 */
@FeignClient(name = "payment-dao-client", url = "${service.payment-dao.base-url}")
public interface PaymentDaoClient {

  @PostMapping(value = "/payments", consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest paymentRequest);

  @GetMapping(value = "/payments/{paymentId}")
  ResponseEntity<PaymentResponse> getPayment(@PathVariable Long paymentId);

  @GetMapping(value = "/payments")
  ResponseEntity<List<PaymentBriefResponse>> getPayments();

  @GetMapping(value = "/payments/inn/{inn}")
  ResponseEntity<List<PaymentClientBriefResponse>> getPayerPaymentsByInn(@PathVariable String inn);

  @GetMapping(value = "/payments/okpo/{okpo}")
  ResponseEntity<List<PaymentClientBriefResponse>> getRecipientPaymentsByOkpo(
      @PathVariable String okpo);

  @GetMapping(value = "/payments/{paymentId}/withdrawals")
  ResponseEntity<List<PaymentWithdrawalResponse>> getPaymentWithdrawals(
      @PathVariable Long paymentId);

  @PostMapping(value = "/payments/{paymentId}/withdrawals")
  ResponseEntity<PaymentWithdrawalResponse> createPaymentWithdrawal(@PathVariable Long paymentId);

  @DeleteMapping(value = "/payment-withdrawals/{paymentWithdrawalId}")
  ResponseEntity<Void> deletePaymentWithdrawal(@PathVariable Long paymentWithdrawalId);
}
