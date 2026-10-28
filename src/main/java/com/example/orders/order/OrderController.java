package com.example.orders.order;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService service;

  public OrderController(OrderService service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public OrderResponse create(@Valid @RequestBody CreateOrderRequest request) {
    return service.create(request);
  }

  @GetMapping
  public List<OrderResponse> list() {
    return service.list();
  }

  @GetMapping("/{id}")
  public OrderResponse get(@PathVariable long id) {
    return service.get(id);
  }

  @PostMapping("/{id}/pay")
  public OrderResponse pay(@PathVariable long id) {
    return service.pay(id);
  }

  @PostMapping("/{id}/ship")
  public OrderResponse ship(@PathVariable long id) {
    return service.ship(id);
  }
}
