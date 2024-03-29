-- Create the inventory database if it does not exist
CREATE DATABASE IF NOT EXISTS inventory;

-- Use the inventory database
USE inventory;

-- Create the products table
CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    quantity INT,
    price DECIMAL(10, 2)
);

-- Insert sample products
INSERT INTO products (name, quantity, price) VALUES ('Sample Product 1', 10, 99.99);
INSERT INTO products (name, quantity, price) VALUES ('Sample Product 2', 20, 149.99);

SELECT * FROM products;