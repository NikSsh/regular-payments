package com.my.paymentregulation.util;

import com.my.paymentregulation.dto.PaymentBriefResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.LongStream;

public class DataGenerator {

  public static List<PaymentBriefResponse> generatePaymentBriefResponses(int size, boolean isWithdrawalNeeded) {
    return LongStream.range(1, size)
        .mapToObj(id -> {
          Duration withdrawalPeriod = Duration.ofDays(1L);
          LocalDateTime now = LocalDateTime.now();
          LocalDateTime lastWithdrawal = isWithdrawalNeeded
              ? now.minusSeconds(withdrawalPeriod.toSeconds())
              : now;
          return new PaymentBriefResponse(id, withdrawalPeriod,
              lastWithdrawal, now);
        })
        .toList();
  }
}
