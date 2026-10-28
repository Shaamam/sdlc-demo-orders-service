package com.example.orders.coupon;

import com.example.orders.common.NotFoundException;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouponService {

  private final CouponRepository coupons;

  public CouponService(CouponRepository coupons) {
    this.coupons = coupons;
  }

  /** Coupon codes are case-insensitive and stored upper-case. */
  public Coupon get(String code) {
    String normalized = code.trim().toUpperCase(Locale.ROOT);
    return coupons
        .findById(normalized)
        .orElseThrow(() -> new NotFoundException("Coupon", normalized));
  }
}
