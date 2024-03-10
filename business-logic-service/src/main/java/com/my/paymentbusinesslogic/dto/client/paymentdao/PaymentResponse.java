package com.my.paymentbusinesslogic.dto.client.paymentdao;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentResponse {

  private Long id;
  private String payerFullName;
  private String payerInn;
  private String payerCardNumber;
  private String recipientAccount;
  private String recipientMfo;
  private String recipientOkpo;
  private String recipientName;
  private BigDecimal amount;
  private Duration withdrawalPeriod;
  private LocalDateTime lastWithdrawal;
  private LocalDateTime createdAt;
}
