BEGIN;

DROP TABLE IF EXISTS users CASCADE;

DROP TABLE IF EXISTS products CASCADE;

DROP TABLE IF EXISTS cards CASCADE;

DROP TABLE IF EXISTS users_products CASCADE;

CREATE TABLE users (id BIGSERIAL PRIMARY KEY, name VARCHAR(255));

INSERT INTO users (name)
VALUES
('Bill'),
('Jack'),
('Kevin'),
('Michael'),
('Ann');

CREATE TABLE products
(
id BIGSERIAL PRIMARY KEY,
title VARCHAR(255) NOT NULL
);

INSERT INTO products (title)
VALUES
('Milk'),
('Cheese'),
('Bread'),
('Pasta'),
('Eggs');

CREATE TABLE cards
(
id BIGSERIAL PRIMARY KEY,
title VARCHAR(255) NOT NULL,
user_id BIGINT,
FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

INSERT INTO cards(title, user_id)
VALUES
('VTB', 1),
('SBER', 1),
('TINKOFF', 2),
('VTB+', 3);

CREATE TABLE users_products
(
user_id BIGINT,
product_id BIGINT,
FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE
);

INSERT INTO users_products (user_id, product_id)
VALUES
(3,1),
(2,3),
(3,5),
(4,2),
(5,3),
(2,1);

COMMIT;