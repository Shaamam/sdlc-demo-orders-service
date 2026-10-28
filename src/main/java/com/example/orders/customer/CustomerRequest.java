package com.example.orders.customer;

import jakarta.validation.constraints.NotBlank;

public record CustomerRequest(@NotBlank String name, @NotBlank String email) {}
