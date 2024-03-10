package com.my.paymentdao.dto.payment;

import java.math.BigDecimal;
import java.time.Duration;
import lombok.Builder;

@Builder
public record PaymentClientBriefResponse(
    Long id,
    Duration withdrawalPeriod,
    BigDecimal amount
) {
}
