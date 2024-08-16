package com.crypto.investment.exception;

/**
 * {@code CSVParsingException} is a custom runtime exception that is thrown when there is an error
 * during the CSV file parsing.
 */
public class CSVParsingException extends RuntimeException {

  public CSVParsingException(String message) {
    super(message);
  }
}
