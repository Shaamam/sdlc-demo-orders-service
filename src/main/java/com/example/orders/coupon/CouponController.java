package com.example.orders.coupon;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

  private final CouponService service;

  public CouponController(CouponService service) {
    this.service = service;
  }

  @GetMapping("/{code}")
  public CouponResponse get(@PathVariable String code) {
    return CouponResponse.from(service.get(code));
  }
}
