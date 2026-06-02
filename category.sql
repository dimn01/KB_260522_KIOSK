CREATE TABLE IF NOT EXISTS CATEGORY (
    categoryId VARCHAR(20) NOT NULL,
    categoryName VARCHAR(50) NOT NULL,
    PRIMARY KEY (categoryId)
);

INSERT INTO CATEGORY (categoryId, categoryName) VALUES
('C1', '라면류'),
('C2', '덮밥/볶음밥류'),
('C3', '카페/음료류'),
('C4', '스낵/과자류')
ON DUPLICATE KEY UPDATE categoryName = VALUES(categoryName);
