package com.my.paymentdao.dto.paymentwithdrawal;

import com.my.paymentdao.persistence.domain.enums.PaymentWithdrawalStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record PaymentWithdrawalResponse(
    Long id,
    BigDecimal amount,
    LocalDateTime createdAt,
    PaymentWithdrawalStatus status) {
}
