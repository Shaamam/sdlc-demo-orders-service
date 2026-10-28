package com.example.orders.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String sku;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String category;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal price;

  protected Product() {}

  public Product(String sku, String name, String category, BigDecimal price) {
    this.sku = sku;
    this.name = name;
    this.category = category;
    this.price = price;
  }

  public Long getId() {
    return id;
  }

  public String getSku() {
    return sku;
  }

  public String getName() {
    return name;
  }

  public String getCategory() {
    return category;
  }

  public BigDecimal getPrice() {
    return price;
  }
}
