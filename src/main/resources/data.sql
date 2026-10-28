-- Seed data for local runs and tests. 30 products so list/pagination demos have something to page through.
INSERT INTO product (sku, name, category, price) VALUES ('BOOK-001', 'Clean Code', 'BOOK', 12.99);
INSERT INTO product (sku, name, category, price) VALUES ('BOOK-002', 'Refactoring', 'BOOK', 45.50);
INSERT INTO product (sku, name, category, price) VALUES ('BOOK-003', 'Domain-Driven Design', 'BOOK', 39.00);
INSERT INTO product (sku, name, category, price) VALUES ('BOOK-004', 'The Pragmatic Programmer', 'BOOK', 29.95);
INSERT INTO product (sku, name, category, price) VALUES ('BOOK-005', 'Release It!', 'BOOK', 34.20);
INSERT INTO product (sku, name, category, price) VALUES ('BOOK-006', 'Accelerate', 'BOOK', 27.80);
INSERT INTO product (sku, name, category, price) VALUES ('BOOK-007', 'Team Topologies', 'BOOK', 31.00);
INSERT INTO product (sku, name, category, price) VALUES ('BOOK-008', 'Designing Data-Intensive Applications', 'BOOK', 49.99);
INSERT INTO product (sku, name, category, price) VALUES ('ELEC-001', 'USB-C Hub', 'ELEC', 24.99);
INSERT INTO product (sku, name, category, price) VALUES ('ELEC-002', 'Mechanical Keyboard', 'ELEC', 89.00);
INSERT INTO product (sku, name, category, price) VALUES ('ELEC-003', 'Noise-Cancelling Headphones', 'ELEC', 249.00);
INSERT INTO product (sku, name, category, price) VALUES ('ELEC-004', '27in Monitor', 'ELEC', 199.99);
INSERT INTO product (sku, name, category, price) VALUES ('ELEC-005', 'Webcam HD', 'ELEC', 59.90);
INSERT INTO product (sku, name, category, price) VALUES ('ELEC-006', 'Wireless Mouse', 'ELEC', 19.99);
INSERT INTO product (sku, name, category, price) VALUES ('ELEC-007', 'Laptop Stand', 'ELEC', 35.00);
INSERT INTO product (sku, name, category, price) VALUES ('ELEC-008', 'Portable SSD 1TB', 'ELEC', 119.00);
INSERT INTO product (sku, name, category, price) VALUES ('HOME-001', 'Coffee Grinder', 'HOME', 79.00);
INSERT INTO product (sku, name, category, price) VALUES ('HOME-002', 'Pour-Over Kettle', 'HOME', 39.90);
INSERT INTO product (sku, name, category, price) VALUES ('HOME-003', 'Desk Lamp', 'HOME', 22.50);
INSERT INTO product (sku, name, category, price) VALUES ('HOME-004', 'Ergonomic Chair', 'HOME', 299.00);
INSERT INTO product (sku, name, category, price) VALUES ('HOME-005', 'Standing Desk Mat', 'HOME', 18.75);
INSERT INTO product (sku, name, category, price) VALUES ('HOME-006', 'Plant Pot', 'HOME', 9.99);
INSERT INTO product (sku, name, category, price) VALUES ('HOME-007', 'Wall Clock', 'HOME', 15.40);
INSERT INTO product (sku, name, category, price) VALUES ('TOYS-001', 'Puzzle 1000pc', 'TOYS', 24.00);
INSERT INTO product (sku, name, category, price) VALUES ('TOYS-002', 'Building Blocks', 'TOYS', 13.37);
INSERT INTO product (sku, name, category, price) VALUES ('TOYS-003', 'Board Game', 'TOYS', 32.10);
INSERT INTO product (sku, name, category, price) VALUES ('TOYS-004', 'Drone Mini', 'TOYS', 64.00);
INSERT INTO product (sku, name, category, price) VALUES ('TOYS-005', 'Rubik Cube', 'TOYS', 11.11);
INSERT INTO product (sku, name, category, price) VALUES ('TOYS-006', 'Kite', 'TOYS', 7.50);
INSERT INTO product (sku, name, category, price) VALUES ('TOYS-007', 'Yo-Yo', 'TOYS', 5.25);

INSERT INTO customer (name, email, registered_at) VALUES ('Asha Rao', 'asha@example.com', TIMESTAMP '2026-01-15 09:30:00');
INSERT INTO customer (name, email, registered_at) VALUES ('Ben Ortiz', 'ben@example.com', TIMESTAMP '2026-03-02 14:05:00');

INSERT INTO coupon (code, percent_off, expires_at) VALUES ('WELCOME10', 10, TIMESTAMP '2099-12-31 23:59:59');
INSERT INTO coupon (code, percent_off, expires_at) VALUES ('SUMMER25', 25, TIMESTAMP '2099-08-31 23:59:59');
INSERT INTO coupon (code, percent_off, expires_at) VALUES ('SPRING15', 15, TIMESTAMP '2020-05-31 23:59:59');
