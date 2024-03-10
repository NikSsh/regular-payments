package com.my.paymentbusinesslogic.api.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.fasterxml.jackson.databind.JsonMappingException;
import feign.FeignException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Global exception handler for handling specific exceptions and providing custom error responses.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  @ExceptionHandler(Exception.class)
  public ResponseEntity<AppErrorResponse> handleException(Exception ex) {
    log.error(ex.getMessage(), ex);

    return new ResponseEntity<>(new AppErrorResponse(INTERNAL_SERVER_ERROR.value(),
        "An unexpected error occurred."), INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(FeignException.class)
  public ResponseEntity<AppErrorResponse> handleFeignStatusException(FeignException e) {
    if (e.status() == INTERNAL_SERVER_ERROR.value()) {
      log.error(e.getMessage(), e);
    } else {
      log.info(e.getMessage());
    }

    return new ResponseEntity<>(new AppErrorResponse(e.status(),
        e.getMessage()), HttpStatus.valueOf(e.status()));
  }

  /**
   * The given method intercepts MethodArgumentTypeMismatchException, which occurs when
   * an argument of a controller method has wrong type. For example, id must have
   * a representation of Long (i.e "5"), but was inconvertible String (i.e "asdf").
   *
   * @param ex TypeMismatchException that is intercepted by this method.
   * @return ResponseEntity with details about TypeMismatchException.
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
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
    return new ResponseEntity<>(new AppErrorResponse(BAD_REQUEST.value(), message), BAD_REQUEST);
  }

  /**
   * This method is intended to handle exceptions related to the inability to
   * read the HTTP message, usually caused by issues in the request payload,
   * particularly during JSON parsing incompatible types (from 'abc' to Integer).
   *
   * @param ex HttpMessageNotReadableException that is intercepted by this method.
   * @return ResponseEntity with details about HttpMessageNotReadableException.
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
    Throwable cause = ex.getCause();
    String errorMessage;
    if (cause instanceof JsonMappingException jsonMappingException) {
      errorMessage = "JSON parse error: " + jsonMappingException.getOriginalMessage();
    } else {
      errorMessage = "Malformed JSON request. Check your request payload.";
    }
    return new ResponseEntity<>(new AppErrorResponse(BAD_REQUEST.value(), errorMessage),
        BAD_REQUEST);
  }

  /**
   * Exception handler method for handling MethodArgumentNotValidException. It processes validation
   * errors and generates a custom response entity with error details.
   *
   * @param ex The MethodArgumentNotValidException instance caught during request processing.
   * @return ResponseEntity containing AppException with details of validation errors.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<AppErrorResponse> handleNotValidRegisterData(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    StringBuilder extractedMessages = new StringBuilder();

    for (String errorMessage : errors.values()) {
      String extractedMessage = errorMessage.replaceAll("\\{(.+?)=(.+?)}", "$2").trim();
      extractedMessages.append(extractedMessage).append(", ");
    }

    String finalMessage = extractedMessages.toString();
    if (finalMessage.endsWith(", ")) {
      finalMessage = finalMessage.substring(0, finalMessage.length() - 2);
    }

    return new ResponseEntity<>(new AppErrorResponse(BAD_REQUEST.value(), finalMessage),
        BAD_REQUEST);
  }
}
