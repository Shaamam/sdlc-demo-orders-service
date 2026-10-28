package com.example.orders.order;

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
import org.springframework.test.web.servlet.MvcResult;

@ApiTest
class OrderApiTest {

  private static final String TWO_BOOKS =
      """
      {"customerEmail": "asha@example.com",
       "items": [{"sku": "BOOK-001", "quantity": 1}, {"sku": "BOOK-002", "quantity": 1}]}
      """;

  @Autowired MockMvc mvc;

  @Test
  void createsAnOrderWithPricesFromTheCatalogue() throws Exception {
    mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content(TWO_BOOKS))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.status").value("CREATED"))
        .andExpect(jsonPath("$.items", hasSize(2)))
        .andExpect(jsonPath("$.items[0].unitPrice").value(12.99))
        .andExpect(jsonPath("$.total").value(58.49));
  }

  @Test
  void listsAndFetchesOrders() throws Exception {
    long id = createOrder();

    mvc.perform(get("/api/orders/{id}", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.customerEmail").value("asha@example.com"));
    mvc.perform(get("/api/orders")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));
  }

  @Test
  void paidOrdersCanBeShipped() throws Exception {
    long id = createOrder();

    mvc.perform(post("/api/orders/{id}/pay", id)).andExpect(jsonPath("$.status").value("PAID"));
    mvc.perform(post("/api/orders/{id}/ship", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("SHIPPED"));
  }

  @Test
  void unpaidOrdersCannotBeShipped() throws Exception {
    long id = createOrder();

    mvc.perform(post("/api/orders/{id}/ship", id))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.code").value("ORDER_INVALID_STATE"));
  }

  @Test
  void ordersCannotBePaidTwice() throws Exception {
    long id = createOrder();
    mvc.perform(post("/api/orders/{id}/pay", id)).andExpect(status().isOk());

    mvc.perform(post("/api/orders/{id}/pay", id)).andExpect(status().isConflict());
  }

  @Test
  void rejectsInvalidRequests() throws Exception {
    mvc.perform(
            post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"customerEmail\": \"not-an-email\", \"items\": []}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors.customerEmail").exists())
        .andExpect(jsonPath("$.errors.items").exists());
  }

  @Test
  void unknownSkuIsNotFound() throws Exception {
    mvc.perform(
            post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"customerEmail\": \"a@example.com\","
                        + " \"items\": [{\"sku\": \"NOPE-1\", \"quantity\": 1}]}"))
        .andExpect(status().isNotFound());
  }

  @Test
  void unknownOrderIsNotFound() throws Exception {
    mvc.perform(get("/api/orders/{id}", 999_999))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.title").value("Not found"));
  }

  private long createOrder() throws Exception {
    MvcResult result =
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content(TWO_BOOKS))
            .andExpect(status().isCreated())
            .andReturn();
    String body = result.getResponse().getContentAsString();
    return Long.parseLong(body.replaceAll("^\\{\"id\":(\\d+).*$", "$1"));
  }
}
