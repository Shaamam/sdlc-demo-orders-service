package com.example.orders.coupon;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.orders.ApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@ApiTest
class CouponApiTest {

  @Autowired MockMvc mvc;

  @Test
  void codesAreCaseInsensitive() throws Exception {
    mvc.perform(get("/api/coupons/welcome10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("WELCOME10"))
        .andExpect(jsonPath("$.percentOff").value(10));
  }

  @Test
  void unknownCouponIsNotFound() throws Exception {
    mvc.perform(get("/api/coupons/NOPE")).andExpect(status().isNotFound());
  }
}
