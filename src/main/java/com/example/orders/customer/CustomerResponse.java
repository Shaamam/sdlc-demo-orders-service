package com.example.orders.customer;

import java.time.Instant;

public record CustomerResponse(long id, String name, String email, Instant registeredAt) {

  static CustomerResponse from(Customer customer) {
    return new CustomerResponse(
        customer.getId(), customer.getName(), customer.getEmail(), customer.getRegisteredAt());
  }
}
