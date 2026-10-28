package com.example.orders.order;

import com.example.orders.common.NotFoundException;
import com.example.orders.product.Product;
import com.example.orders.product.ProductService;
import java.time.Clock;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

  private final OrderRepository orders;
  private final ProductService products;
  private final OrderPricing pricing;
  private final Clock clock;

  public OrderService(
      OrderRepository orders, ProductService products, OrderPricing pricing, Clock clock) {
    this.orders = orders;
    this.products = products;
    this.pricing = pricing;
    this.clock = clock;
  }

  @Transactional
  public OrderResponse create(CreateOrderRequest request) {
    List<OrderItem> items =
        request.items().stream()
            .map(
                line -> {
                  Product product = products.getBySku(line.sku());
                  return new OrderItem(product.getSku(), line.quantity(), product.getPrice());
                })
            .toList();
    Order order = orders.save(new Order(request.customerEmail(), items, clock.instant()));
    return toResponse(order);
  }

  public OrderResponse get(long id) {
    return toResponse(find(id));
  }

  public List<OrderResponse> list() {
    return orders.findAll(Sort.by("id")).stream().map(this::toResponse).toList();
  }

  @Transactional
  public OrderResponse pay(long id) {
    Order order = find(id);
    order.pay();
    return toResponse(order);
  }

  @Transactional
  public OrderResponse ship(long id) {
    Order order = find(id);
    order.ship();
    return toResponse(order);
  }

  private Order find(long id) {
    return orders.findById(id).orElseThrow(() -> new NotFoundException("Order", id));
  }

  private OrderResponse toResponse(Order order) {
    return OrderResponse.from(order, pricing.total(order.getItems()));
  }
}
