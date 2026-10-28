package com.example.orders.common;

/**
 * Thrown when a request is valid but conflicts with the current state of a resource (for example
 * shipping an order that was never paid). Mapped to HTTP 409.
 */
public class BusinessRuleException extends RuntimeException {

  private final String code;

  public BusinessRuleException(String code, String message) {
    super(message);
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
