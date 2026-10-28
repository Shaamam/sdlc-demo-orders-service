package com.example.orders.customer;

import com.example.orders.common.NotFoundException;
import java.time.Clock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CustomerService {

  private final CustomerRepository customers;
  private final Clock clock;

  public CustomerService(CustomerRepository customers, Clock clock) {
    this.customers = customers;
    this.clock = clock;
  }

  @Transactional
  public Customer register(CustomerRequest request) {
    return customers.save(new Customer(request.name(), request.email(), clock.instant()));
  }

  public Customer get(long id) {
    return customers.findById(id).orElseThrow(() -> new NotFoundException("Customer", id));
  }
}
