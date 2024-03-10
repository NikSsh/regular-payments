package com.my.paymentregulation.service;

import com.my.paymentregulation.client.PaymentBusinessLogicClient;
import com.my.paymentregulation.dto.PaymentBriefResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PaymentWithdrawalService {
  private final PaymentBusinessLogicClient paymentBusinessLogicClient;

  public void initiatePaymentWithdrawalProcess() {
    List<PaymentBriefResponse> payments = paymentBusinessLogicClient.getPayments().getBody();
    payments.parallelStream().forEach(payment ->
      CompletableFuture.runAsync(() -> {
        if (isWithdrawalNeeded(payment)) {
          paymentBusinessLogicClient.createPaymentWithdrawal(payment.id());
        }
    }));
  }

  private boolean isWithdrawalNeeded(PaymentBriefResponse payment) {
    return LocalDateTime.now().isAfter(
        Objects.nonNull(payment.lastWithdrawal())
            ? payment.lastWithdrawal().plusSeconds(payment.withdrawalPeriod().getSeconds())
            : Objects.requireNonNull(payment.createdAt()).plusSeconds(payment.withdrawalPeriod().getSeconds()));
  }
}
