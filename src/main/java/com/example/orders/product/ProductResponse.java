package com.example.orders.product;

import java.math.BigDecimal;

public record ProductResponse(long id, String sku, String name, String category, BigDecimal price) {

  static ProductResponse from(Product product) {
    return new ProductResponse(
        product.getId(),
        product.getSku(),
        product.getName(),
        product.getCategory(),
        product.getPrice());
  }
}
