package com.example.orders.product;

import com.example.orders.common.BusinessRuleException;
import com.example.orders.common.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

  private final ProductRepository products;

  public ProductService(ProductRepository products) {
    this.products = products;
  }

  public List<Product> listAll() {
    return products.findAll(Sort.by("id"));
  }

  public Product get(long id) {
    return products.findById(id).orElseThrow(() -> new NotFoundException("Product", id));
  }

  public Product getBySku(String sku) {
    return products.findBySku(sku).orElseThrow(() -> new NotFoundException("Product", sku));
  }

  @Transactional
  public Product create(ProductRequest request) {
    if (products.existsBySku(request.sku())) {
      throw new BusinessRuleException(
          "PRODUCT_SKU_TAKEN", "A product with SKU " + request.sku() + " already exists");
    }
    return products.save(
        new Product(request.sku(), request.name(), request.category(), request.price()));
  }
}
