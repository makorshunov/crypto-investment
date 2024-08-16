package com.crypto.investment.exception;

/**
 * {@code RateLimitException} is a custom runtime exception thrown when a client exceeds the allowed
 * number of requests within a specified time window.
 */
public class RateLimitException extends RuntimeException {

  public RateLimitException() {
    super("Too many requests from your IP address. Please try again later.");
  }
}
