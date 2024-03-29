package com.my.paymentdao.service.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends GenericException {
  public EntityNotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND);
  }
}
