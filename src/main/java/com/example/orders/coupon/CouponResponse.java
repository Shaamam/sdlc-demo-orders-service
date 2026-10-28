package com.example.orders.coupon;

import java.time.Instant;

public record CouponResponse(String code, int percentOff, Instant expiresAt) {

  static CouponResponse from(Coupon coupon) {
    return new CouponResponse(coupon.getCode(), coupon.getPercentOff(), coupon.getExpiresAt());
  }
}
