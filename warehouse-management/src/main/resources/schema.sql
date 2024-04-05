/*

    used by the h2 database duing application startup
    
    creates our in-memory database tables

*/

DROP TABLE IF EXISTS warehouses CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS inventory CASCADE;
DROP TYPE  IF EXISTS product_size CASCADE;

CREATE TYPE product_size AS ENUM ('SMALL', 'MEDIUM', 'LARGE', 'EXTRA_LARGE');

CREATE TABLE warehouses (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE,
  description TEXT,
  current_capacity INTEGER NOT NULL DEFAULT 0,
  max_capacity INTEGER NOT NULL
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

/* ------------------------------------------------------ */

-- Trigger function for inserting new products into the Products table
/* CREATE OR REPLACE FUNCTION insert_product_function()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO products (product_id, name, description, price, category)
    VALUES (NEW.product_id, NEW.name, NEW.description, NEW.price, NEW.category)
    ON CONFLICT DO NOTHING;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Trigger for inserting new products into the Products table
CREATE TRIGGER insert_product_trigger
AFTER INSERT ON inventory
FOR EACH ROW
EXECUTE FUNCTION insert_product_function();

-- Trigger function for deleting products from the Products table
CREATE OR REPLACE FUNCTION delete_product_function()
RETURNS TRIGGER AS $$
BEGIN
    DELETE FROM products
    WHERE product_id = OLD.product_id
    AND NOT EXISTS (SELECT 1 FROM inventory WHERE product_id = OLD.product_id);
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Trigger for deleting products from the Products table when the last instance is removed from inventory
CREATE TRIGGER delete_product_trigger
AFTER DELETE ON inventory
FOR EACH ROW
EXECUTE FUNCTION delete_product_function(); */