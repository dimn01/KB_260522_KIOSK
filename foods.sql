-- 음식 테이블 생성
CREATE TABLE IF NOT EXISTS food (
    food_id VARCHAR(50) PRIMARY KEY,
    category_id VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    price INT NOT NULL,
    stock INT NOT NULL,
    description VARCHAR(255),
    FOREIGN KEY (category_id) REFERENCES category(category_id) ON UPDATE CASCADE ON DELETE RESTRICT
);

-- 초기 음식 데이터 삽입
INSERT INTO food (food_id, category_id, name, price, stock, description) VALUES
('R001', 'C1', '신라면', 4000, 99, '기본 신라면'),
('R002', 'C1', '진라면(매운맛)', 4000, 100, '칼칼한 진라면'),
('R003', 'C1', '까르보불닭볶음면', 5000, 80, '매운 까르보나라'),
('B001', 'C2', '제육덮밥', 7500, 50, '가성비 제육볶음'),
('B002', 'C2', '치킨마요덮밥', 6500, 50, '단짠 치킨마요'),
('B003', 'C2', '통삼겹짜글이', 8000, 40, '삼겹살이 통째로 들어간 짜글이'),
('D001', 'C3', '몬스터에너지', 3000, 50, '고카페인 음료'),
('D002', 'C3', '콜라', 2000, 200, '시원한 콜라'),
('D003', 'C3', '사이다', 4000, 80, '시원한 사이다'),
('S001', 'C4', '포카칩', 1500, 50, '근본 과자'),
('S002', 'C4', '새우깡', 1500, 50, '고소한 새우깡'),
('S003', 'C4', '허니버터칩', 1200, 60, '달콤한 꿀이 곁들어진 과자')
ON DUPLICATE KEY UPDATE 
    category_id = VALUES(category_id),
    name = VALUES(name),
    price = VALUES(price),
    stock = VALUES(stock),
    description = VALUES(description);
