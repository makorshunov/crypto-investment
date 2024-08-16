package com.crypto.investment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * The {@code AppExceptionHandler} class is a global exception handler for handling various
 * exceptions that occur within the application.
 */
@ControllerAdvice
public class AppExceptionHandler {

  /**
   * Handles {@link RateLimitException}
   *
   * @param ex The {@link RateLimitException} that was thrown.
   * @return A {@code ResponseEntity} containing an {@link ExceptionInformation} object with the
   * exception message.
   */
  @ExceptionHandler(RateLimitException.class)
  public ResponseEntity<ExceptionInformation> handleRateLimitException(RateLimitException ex) {
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
        .body(new ExceptionInformation(ex.getMessage()));
  }

  /**
   * Handles {@link NotSupportedCoinException}
   *
   * @param ex The {@link NotSupportedCoinException} that was thrown.
   * @return A {@code ResponseEntity} containing an {@link ExceptionInformation} object with the
   * exception message.
   */
  @ExceptionHandler(NotSupportedCoinException.class)
  public ResponseEntity<ExceptionInformation> handleCoinValidationException(
      NotSupportedCoinException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ExceptionInformation(ex.getMessage()));
  }

  /**
   * Handles {@link RuntimeException}
   *
   * @param ex The {@link RuntimeException} that was thrown.
   * @return A {@code ResponseEntity} containing an {@link ExceptionInformation} object with the
   * exception message.
   */
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ExceptionInformation> handleRuntimeException(RuntimeException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ExceptionInformation(ex.getMessage()));
  }
}
