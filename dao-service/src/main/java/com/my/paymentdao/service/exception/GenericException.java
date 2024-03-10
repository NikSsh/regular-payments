package com.my.paymentdao.service.exception;

import org.springframework.http.HttpStatus;

public abstract class GenericException extends RuntimeException {
  protected HttpStatus status;

  protected GenericException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

  public HttpStatus getStatus() {
    return this.status;
  }
}
