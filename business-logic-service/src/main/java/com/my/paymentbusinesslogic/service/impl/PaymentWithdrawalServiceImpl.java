package com.my.paymentbusinesslogic.service.impl;

import com.my.paymentbusinesslogic.dto.client.paymentdao.PaymentResponse;
import com.my.paymentbusinesslogic.dto.client.paymentdao.PaymentWithdrawalResponse;
import com.my.paymentbusinesslogic.service.PaymentWithdrawalService;
import com.my.paymentbusinesslogic.service.client.PaymentDaoClient;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PaymentWithdrawalServiceImpl implements PaymentWithdrawalService {
  private final PaymentDaoClient paymentDaoClient;

  @Override
  public PaymentWithdrawalResponse createPaymentWithdrawal(Long paymentId) {
    return paymentDaoClient.createPaymentWithdrawal(paymentId).getBody();
  }

  @Override
  public void deletePaymentWithdrawal(Long paymentWithdrawalId) {
    paymentDaoClient.deletePaymentWithdrawal(paymentWithdrawalId);
  }

  @Override
  public List<PaymentWithdrawalResponse> getPaymentWithdrawalsByPaymentId(Long paymentId) {
    return paymentDaoClient.getPaymentWithdrawals(paymentId).getBody();
  }

  @Override
  public boolean isPaymentWithdrawalNeeded(Long paymentId) {
    PaymentResponse payment = paymentDaoClient.getPayment(paymentId).getBody();

    return LocalDateTime.now().isAfter(
        Objects.nonNull(payment.getLastWithdrawal())
            ? payment.getLastWithdrawal().plusSeconds(payment.getWithdrawalPeriod().getSeconds())
            : payment.getCreatedAt().plusSeconds(payment.getWithdrawalPeriod().getSeconds()));
  }
}
