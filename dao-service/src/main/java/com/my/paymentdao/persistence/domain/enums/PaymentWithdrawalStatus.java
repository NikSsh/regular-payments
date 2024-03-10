package com.my.paymentdao.persistence.domain.enums;

import lombok.Getter;

@Getter
public enum PaymentWithdrawalStatus {
  A("Active"),
  S("Stornowed");

  private final String name;

  private PaymentWithdrawalStatus(String name) {
    this.name = name;
  }
}
