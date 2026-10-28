package com.example.orders.order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.stereotype.Component;

/** Money rules for orders. All amounts are BigDecimal with scale 2, rounded HALF_UP. */
@Component
public class OrderPricing {

  public BigDecimal total(List<OrderItem> items) {
    return items.stream()
        .map(OrderItem::getUnitPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add)
        .setScale(2, RoundingMode.HALF_UP);
  }
}
