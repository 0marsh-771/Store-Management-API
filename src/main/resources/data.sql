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

TRUNCATE TABLE products;
TRUNCATE TABLE sales;

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

CREATE TABLE IF NOT EXISTS Roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS Staff (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role_id INT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES Roles(id) ON DELETE CASCADE
    );

INSERT INTO Roles (id, name) VALUES
(1, 'ROLE_CASHIER'),
(2, 'ROLE_MANAGER')
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO Staff (id, name, password, role_id) VALUES
(1, 'John Smith',    '{bcrypt}$2a$10$EOs.BbxdfhDKytrqhSSJx.dHpIEu6XAEchcEZkm7PDcKEcfhlh3Aq', 1),
(2, 'Emily Johnson', '{bcrypt}$2a$10$EOs.BbxdfhDKytrqhSSJx.dHpIEu6XAEchcEZkm7PDcKEcfhlh3Aq', 1),
(3, 'Michael Brown', '{bcrypt}$2a$10$EOs.BbxdfhDKytrqhSSJx.dHpIEu6XAEchcEZkm7PDcKEcfhlh3Aq', 1),
(4, 'Sarah Davis',   '{bcrypt}$2a$10$EOs.BbxdfhDKytrqhSSJx.dHpIEu6XAEchcEZkm7PDcKEcfhlh3Aq', 2),
(5, 'David Wilson',  '{bcrypt}$2a$10$EOs.BbxdfhDKytrqhSSJx.dHpIEu6XAEchcEZkm7PDcKEcfhlh3Aq', 2)
ON DUPLICATE KEY UPDATE name = VALUES(name), password = VALUES(password), role_id = VALUES(role_id);