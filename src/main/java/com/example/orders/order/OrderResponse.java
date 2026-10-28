package com.example.orders.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
    long id,
    String customerEmail,
    OrderStatus status,
    Instant createdAt,
    List<Line> items,
    BigDecimal total) {

  public record Line(String sku, int quantity, BigDecimal unitPrice) {}

  static OrderResponse from(Order order, BigDecimal total) {
    List<Line> lines =
        order.getItems().stream()
            .map(item -> new Line(item.getSku(), item.getQuantity(), item.getUnitPrice()))
            .toList();
    return new OrderResponse(
        order.getId(),
        order.getCustomerEmail(),
        order.getStatus(),
        order.getCreatedAt(),
        lines,
        total);
  }
}
