package com.my.paymentbusinesslogic.api.handler;

import java.time.LocalDateTime;
import lombok.Getter;

/**
 * Represents an application-specific error response with status, message, and timestamp information.
 */
@Getter
public class AppErrorResponse {

  private final int status;
  private final String message;
  private final LocalDateTime timestamp;

  /**
   * Constructs an AppErrorResponse object with the provided status and message, initializing the
   * timestamp to the current time.
   *
   * @param status  HTTP status code associated with the exception.
   * @param message Message describing the exception.
   */
  public AppErrorResponse(int status, String message) {
    this.status = status;
    this.message = message;
    this.timestamp = LocalDateTime.now();
  }
}