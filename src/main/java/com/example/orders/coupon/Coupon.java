package com.example.orders.coupon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.Instant;

@Entity
public class Coupon {

  @Id private String code;

  @Column(nullable = false)
  private int percentOff;

  @Column(nullable = false)
  private Instant expiresAt;

  protected Coupon() {}

  public Coupon(String code, int percentOff, Instant expiresAt) {
    this.code = code;
    this.percentOff = percentOff;
    this.expiresAt = expiresAt;
  }

  public String getCode() {
    return code;
  }

  public int getPercentOff() {
    return percentOff;
  }

  public Instant getExpiresAt() {
    return expiresAt;
  }
}
