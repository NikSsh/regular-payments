package com.my.paymentdao.api.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.my.paymentdao.service.exception.GenericException;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({Exception.class})
  public ResponseEntity<AppErrorResponse> handleException(Exception ex) {
    log.error(ex.getMessage(), ex);
    return new ResponseEntity<>(new AppErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "An unexpected error occurred."), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({GenericException.class})
  public ResponseEntity<AppErrorResponse> handleGenericException(GenericException ex) {
    log.info(ex.getMessage());
    return new ResponseEntity<>(new AppErrorResponse(ex.getStatus().value(), ex.getMessage()),
        ex.getStatus());
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public ResponseEntity<AppErrorResponse> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException ex) {
    String actualField = ex.getName();
    Object wrongValue = Optional.ofNullable(ex.getValue()).orElse("");
    String requiredType = "";
    Class<?> requiredTypeClass = ex.getRequiredType();
    if (Objects.nonNull(requiredTypeClass)) {
      requiredType = requiredTypeClass.getSimpleName();
    }

    String message =
        String.format("The field '%s' must have a valid type of '%s'. Wrong value is '%s'",
            actualField, requiredType, wrongValue);
    return new ResponseEntity<>(new AppErrorResponse(HttpStatus.BAD_REQUEST.value(), message),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({DataIntegrityViolationException.class})
  public ResponseEntity<AppErrorResponse> handleDataIntegrityViolationException(
      DataIntegrityViolationException ex) {
    Throwable rootCause = ex.getRootCause();
    String message = Objects.requireNonNullElse(rootCause, ex).getMessage();
    return new ResponseEntity<>(new AppErrorResponse(HttpStatus.BAD_REQUEST.value(), message),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({HttpMessageNotReadableException.class})
  public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
    Throwable cause = ex.getCause();
    String errorMessage;
    if (cause instanceof JsonMappingException jsonMappingException) {
      errorMessage = "JSON parse error: " + jsonMappingException.getOriginalMessage();
    } else {
      errorMessage = "Malformed JSON request. Check your request payload.";
    }

    return new ResponseEntity<>(new AppErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage),
        HttpStatus.BAD_REQUEST);
  }
}
