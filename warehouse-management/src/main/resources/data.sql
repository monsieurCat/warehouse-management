/*

CREATE TYPE product_size AS ENUM ('SMALL', 'MEDIUM', 'LARGE', 'EXTRA_LARGE');

CREATE TABLE warehouses (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE,
  description TEXT,
  current_capacity INTEGER NOT NULL DEFAULT 0,
  max_capacity INTEGER NOT NULL,
);

CREATE TABLE categories (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE products (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE,
  price DECIMAL(10, 2) NOT NULL,
  category_id INT NOT NULL,
  size product_size NOT NULL,
  description TEXT,
  FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE inventory (
  product_id INT NOT NULL REFERENCES products(id),
  warehouse_id INT NOT NULL REFERENCES warehouses(id),
  quantity INT NOT NULL,
  PRIMARY KEY (warehouse_id, product_id)
);

*/


INSERT INTO warehouses (name, description, max_capacity) VALUES ('warehouse1', 'main warehouse', 200);
INSERT INTO warehouses (name, description, max_capacity) VALUES ('warehouse2', 'secondary warehouse', 100);
/* 
INSERT INTO products (name, price, category_id, size) VALUES ('TV', 1200, 1, 'Large');
INSERT INTO products (name, price, category_id, size) VALUES ('Computer', 800, 1, 'Medium');
INSERT INTO products (name, price, category_id, size) VALUES ('Sofa', 1200, 2, 'ExtraLarge');
INSERT INTO products (name, price, category_id, size) VALUES ('The Hobbit', 8, 3, 'Small'); */