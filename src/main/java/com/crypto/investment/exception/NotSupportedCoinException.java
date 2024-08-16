package com.crypto.investment.exception;

/**
 * {@code NotSupportedCoinException} is a custom runtime exception that is thrown when a requested
 * coin is not supported by the application.
 */
public class NotSupportedCoinException extends RuntimeException {

  public NotSupportedCoinException(String message) {
    super(message);
  }
}
