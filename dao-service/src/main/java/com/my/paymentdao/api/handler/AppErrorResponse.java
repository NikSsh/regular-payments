package com.my.paymentdao.api.handler;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AppErrorResponse {
  private final int status;
  private final String message;
  private final LocalDateTime timestamp;

  public AppErrorResponse(int status, String message) {
    this.status = status;
    this.message = message;
    this.timestamp = LocalDateTime.now();
  }
}
