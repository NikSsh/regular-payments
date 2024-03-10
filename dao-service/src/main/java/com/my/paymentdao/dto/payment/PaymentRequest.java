package com.my.paymentdao.dto.payment;

import java.math.BigDecimal;
import java.time.Duration;
import lombok.Builder;

@Builder
public record PaymentRequest(
    String payerFullName,
    String payerInn,
    String payerCardNumber,
    String recipientAccount,
    String recipientMfo,
    String recipientOkpo,
    String recipientName,
    BigDecimal amount,
    Duration withdrawalPeriod
) {
}
