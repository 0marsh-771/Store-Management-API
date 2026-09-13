-- Переключаемся на вашу базу данных
USE store_directory;

-- 1. Создание таблицы товаров (products)
CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(255),
    name VARCHAR(255),
    price DECIMAL(38,2),
    quantity INT
    );

-- 2. Создание таблицы продаж (sales)
CREATE TABLE IF NOT EXISTS sales (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cashier VARCHAR(255),
    date DATE,
    total DECIMAL(38,2)
    );

-- Очистка таблиц перед заполнением (чтобы не дублировать ID при повторном запуске)
TRUNCATE TABLE products;
TRUNCATE TABLE sales;

-- 3. Добавление 5 товаров в таблицу products
INSERT INTO products (category, name, price, quantity) VALUES
('Electronics', 'Laptop Lenovo IdeaPad', 750.00, 15),
('Electronics', 'Wireless Mouse', 25.50, 50),
('Appliances', 'Coffee Maker', 120.00, 8),
('Gadgets', 'Smartwatch Fitness Tracker', 89.99, 20),
('Accessories', 'USB-C Cable 2m', 12.00, 100);

-- 4. Добавление 5 записей о продажах в таблицу sales
INSERT INTO sales (cashier, date, total) VALUES
('John Doe', '2026-09-01', 775.50),
('Jane Smith', '2026-09-02', 120.00),
('John Doe', '2026-09-05', 89.99),
('Alex Johnson', '2026-09-10', 37.50),
('Jane Smith', '2026-09-12', 250.00);