package com.crypto.investment.exception;

/**
 * {@code CSVImportException} is a custom runtime exception that is thrown when there is an error
 * during the CSV files import
 */
public class CSVImportException extends RuntimeException {

  public CSVImportException(String message) {
    super(message);
  }
}
