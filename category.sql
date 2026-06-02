-- 카테고리 테이블 생성
CREATE TABLE IF NOT EXISTS category (
    category_id VARCHAR(50) PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL
);

-- 초기 카테고리 데이터 삽입
INSERT INTO category (category_id, category_name) VALUES
('C1', '라면류'),
('C2', '덮밥/볶음밥류'),
('C3', '카페/음료류'),
('C4', '스낵/과자류')
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name);
