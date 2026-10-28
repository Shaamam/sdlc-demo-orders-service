package com.example.orders.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CreateOrderRequest(
    @NotBlank @Email String customerEmail, @NotEmpty List<@Valid Line> items) {

  public record Line(@NotBlank String sku, @Min(1) int quantity) {}
}
