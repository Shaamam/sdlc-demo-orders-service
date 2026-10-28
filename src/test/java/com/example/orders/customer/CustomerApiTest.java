package com.example.orders.customer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.orders.ApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@ApiTest
class CustomerApiTest {

  @Autowired MockMvc mvc;

  @Test
  void registersACustomer() throws Exception {
    mvc.perform(
            post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Chen Li\", \"email\": \"chen@example.com\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.registeredAt").exists());
  }

  @Test
  void fetchesACustomer() throws Exception {
    mvc.perform(get("/api/customers/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("asha@example.com"));
  }

  @Test
  void unknownCustomerIsNotFound() throws Exception {
    mvc.perform(get("/api/customers/999")).andExpect(status().isNotFound());
  }

  @Test
  void nameIsRequired() throws Exception {
    mvc.perform(
            post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \" \", \"email\": \"x@example.com\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors.name").exists());
  }
}
