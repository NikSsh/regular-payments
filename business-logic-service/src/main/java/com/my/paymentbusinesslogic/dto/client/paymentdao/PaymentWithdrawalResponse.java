package com.my.paymentbusinesslogic.dto.client.paymentdao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentWithdrawalResponse(
    Long id,
    BigDecimal amount,
    LocalDateTime createdAt,
    String status
) {
}
