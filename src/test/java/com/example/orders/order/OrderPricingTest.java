package com.example.orders.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrderPricingTest {

  private final OrderPricing pricing = new OrderPricing();

  @Test
  void emptyOrderCostsNothing() {
    assertThat(pricing.total(List.of())).isEqualByComparingTo("0.00");
  }

  @Test
  void singleItemCostsItsUnitPrice() {
    List<OrderItem> items = List.of(new OrderItem("BOOK-001", 1, new BigDecimal("12.99")));

    assertThat(pricing.total(items)).isEqualTo(new BigDecimal("12.99"));
  }

  @Test
  void differentItemsAreAddedTogether() {
    List<OrderItem> items =
        List.of(
            new OrderItem("BOOK-001", 1, new BigDecimal("12.99")),
            new OrderItem("BOOK-002", 1, new BigDecimal("45.50")));

    assertThat(pricing.total(items)).isEqualTo(new BigDecimal("58.49"));
  }
}
