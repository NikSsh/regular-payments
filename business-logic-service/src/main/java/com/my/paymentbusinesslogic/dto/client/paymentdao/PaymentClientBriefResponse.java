package com.my.paymentbusinesslogic.dto.client.paymentdao;

import java.math.BigDecimal;
import java.time.Duration;

public record PaymentClientBriefResponse(
    Long id,
    Duration withdrawalPeriod,
    BigDecimal amount
) {
}
