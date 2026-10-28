# orders-service API

Every endpoint the service exposes. CI's `api-docs` check fails if a controller mapping is missing here,
so add a row whenever you add or change an endpoint.

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/products` | List the whole catalogue, ordered by id |
| `GET` | `/api/products/{id}` | One product; 404 if unknown |
| `POST` | `/api/products` | Create a product; 409 `PRODUCT_SKU_TAKEN` if the SKU exists |
| `POST` | `/api/orders` | Place an order; unit prices come from the catalogue |
| `GET` | `/api/orders` | List orders, ordered by id |
| `GET` | `/api/orders/{id}` | One order; 404 if unknown |
| `POST` | `/api/orders/{id}/pay` | CREATED → PAID; 409 `ORDER_INVALID_STATE` otherwise |
| `POST` | `/api/orders/{id}/ship` | PAID → SHIPPED; 409 `ORDER_INVALID_STATE` otherwise |
| `POST` | `/api/customers` | Register a customer |
| `GET` | `/api/customers/{id}` | One customer; 404 if unknown |
| `GET` | `/api/coupons/{code}` | One coupon (code is case-insensitive); 404 if unknown |

Errors use RFC 9457 problem details: `{"title", "status", "detail"}` plus `code` for 409s and an
`errors` map (field → message) for 400s.
