package com.example.orders.product;

import static org.hamcrest.Matchers.hasSize;
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
class ProductApiTest {

  @Autowired MockMvc mvc;

  @Test
  void listsTheWholeCatalogueOrderedById() throws Exception {
    mvc.perform(get("/api/products"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(30)))
        .andExpect(jsonPath("$[0].sku").value("BOOK-001"))
        .andExpect(jsonPath("$[29].sku").value("TOYS-007"));
  }

  @Test
  void fetchesOneProduct() throws Exception {
    mvc.perform(get("/api/products/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Clean Code"));
  }

  @Test
  void unknownProductIsNotFound() throws Exception {
    mvc.perform(get("/api/products/999")).andExpect(status().isNotFound());
  }

  @Test
  void createsAProduct() throws Exception {
    mvc.perform(
            post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"sku\": \"BOOK-100\", \"name\": \"Working Effectively with Legacy Code\","
                        + " \"category\": \"BOOK\", \"price\": 41.00}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNumber());
  }

  @Test
  void rejectsDuplicateSku() throws Exception {
    mvc.perform(
            post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"sku\": \"BOOK-001\", \"name\": \"Copy\", \"category\": \"BOOK\","
                        + " \"price\": 1.00}"))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.code").value("PRODUCT_SKU_TAKEN"));
  }

  @Test
  void rejectsNonPositivePrice() throws Exception {
    mvc.perform(
            post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"sku\": \"X-1\", \"name\": \"Free\", \"category\": \"TOYS\", \"price\": 0}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors.price").exists());
  }
}
