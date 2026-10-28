package com.example.orders.order;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class OrderItem {

  @Column(nullable = false)
  private String sku;

  @Column(nullable = false)
  private int quantity;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal unitPrice;

  protected OrderItem() {}

  public OrderItem(String sku, int quantity, BigDecimal unitPrice) {
    this.sku = sku;
    this.quantity = quantity;
    this.unitPrice = unitPrice;
  }

  public String getSku() {
    return sku;
  }

  public int getQuantity() {
    return quantity;
  }

  public BigDecimal getUnitPrice() {
    return unitPrice;
  }
}
