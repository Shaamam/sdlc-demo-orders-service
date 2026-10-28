package com.example.orders.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ProductRequest(
    @NotBlank String sku,
    @NotBlank String name,
    @NotBlank String category,
    @NotNull @DecimalMin("0.01") @Digits(integer = 10, fraction = 2) BigDecimal price) {}
