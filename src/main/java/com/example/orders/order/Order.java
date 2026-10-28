package com.example.orders.order;

import com.example.orders.common.BusinessRuleException;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String customerEmail;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrderStatus status;

  @Column(nullable = false)
  private Instant createdAt;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
  private List<OrderItem> items = new ArrayList<>();

  protected Order() {}

  public Order(String customerEmail, List<OrderItem> items, Instant createdAt) {
    this.customerEmail = customerEmail;
    this.items = new ArrayList<>(items);
    this.createdAt = createdAt;
    this.status = OrderStatus.CREATED;
  }

  /** Marks the order as paid. Only a freshly created order can be paid. */
  public void pay() {
    requireStatus(OrderStatus.CREATED, "pay");
    status = OrderStatus.PAID;
  }

  /** Marks the order as shipped. Only a paid order can be shipped. */
  public void ship() {
    requireStatus(OrderStatus.PAID, "ship");
    status = OrderStatus.SHIPPED;
  }

  private void requireStatus(OrderStatus expected, String action) {
    if (status != expected) {
      throw new BusinessRuleException(
          "ORDER_INVALID_STATE",
          "Cannot " + action + " order " + id + " in status " + status + "; expected " + expected);
    }
  }

  public Long getId() {
    return id;
  }

  public String getCustomerEmail() {
    return customerEmail;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public List<OrderItem> getItems() {
    return List.copyOf(items);
  }
}
