package com.my.paymentdao.dto.payment;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record PaymentBriefResponse (
    Long id,
    Duration withdrawalPeriod,
    LocalDateTime lastWithdrawal,
    LocalDateTime createdAt
){
}
