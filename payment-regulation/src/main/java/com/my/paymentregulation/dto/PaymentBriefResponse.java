package com.my.paymentregulation.dto;

import java.time.Duration;
import java.time.LocalDateTime;

public record PaymentBriefResponse(
    Long id,
    Duration withdrawalPeriod,
    LocalDateTime lastWithdrawal,
    LocalDateTime createdAt
) {
}
