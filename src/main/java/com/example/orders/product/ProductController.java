package com.example.orders.product;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService service;

  public ProductController(ProductService service) {
    this.service = service;
  }

  /** Returns every product in the catalogue, ordered by id. */
  @GetMapping
  public List<ProductResponse> list() {
    return service.listAll().stream().map(ProductResponse::from).toList();
  }

  @GetMapping("/{id}")
  public ProductResponse get(@PathVariable long id) {
    return ProductResponse.from(service.get(id));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProductResponse create(@Valid @RequestBody ProductRequest request) {
    return ProductResponse.from(service.create(request));
  }
}
