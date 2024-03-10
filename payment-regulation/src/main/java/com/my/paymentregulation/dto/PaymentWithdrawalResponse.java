package com.my.paymentregulation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentWithdrawalResponse(
    BigDecimal amount,
    LocalDateTime createdAt,
    String status
) {
}
