package com.my.paymentregulation.client;

import com.my.paymentregulation.dto.PaymentBriefResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Feign client interface for communicating with the payment business logic service.
 */
@FeignClient(name = "payment-business-logic-client", url = "${service.payment-business-logic.base-url}")
public interface PaymentBusinessLogicClient {

  @GetMapping(value = "/api/v1/payments")
  List<PaymentBriefResponse> getPayments();

  @PostMapping(value = "/api/v1/payments/{paymentId}/withdrawals")
  void createPaymentWithdrawal(@PathVariable Long paymentId);
}
