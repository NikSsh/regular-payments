package com.my.paymentdao.util;

import com.my.paymentdao.persistence.domain.entity.Payment;
import com.my.paymentdao.persistence.domain.entity.PaymentWithdrawal;
import com.my.paymentdao.persistence.domain.enums.PaymentWithdrawalStatus;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class DataGenerator {

  public static Payment createValidPayment(Long id) {
    return Payment.builder()
        .id(id)
        .payerFullName("Martin Luther King")
        .payerInn("1246993119")
        .payerCardNumber("4012888888881888")
        .recipientAccount("UA213223130000026007233566001")
        .recipientMfo("123456")
        .recipientOkpo("38528147")
        .recipientName("Recipient Company")
        .amount(BigDecimal.valueOf(1000.50))
        .withdrawalPeriod(Duration.parse("PT1H30M"))
        .build();
  }

  public static List<PaymentWithdrawal> createPaymentWithdrawals(Payment payment, int size) {
    return LongStream.rangeClosed(0, size)
        .mapToObj(index -> createPaymentWithdrawal(index, payment))
        .toList();
  }

  public static PaymentWithdrawal createPaymentWithdrawal(Long id, Payment payment) {
    return PaymentWithdrawal.builder()
        .id(id)
        .payment(payment)
        .amount(payment.getAmount())
        .status(PaymentWithdrawalStatus.A)
        .createdAt(LocalDateTime.now())
        .build();
  }
}
