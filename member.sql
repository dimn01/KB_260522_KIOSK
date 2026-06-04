USE kiosk;

-- 1. 외래 키 체크 일시 정지
SET FOREIGN_KEY_CHECKS = 0;

-- 2. 안전하게 삭제
DROP TABLE IF EXISTS CART;
DROP TABLE IF EXISTS MEMBERS;

-- 3. 테이블 다시 생성
CREATE TABLE MEMBERS (
                         memberId VARCHAR(50) NOT NULL,
                         password VARCHAR(255) NOT NULL,
                         email VARCHAR(100),
                         name VARCHAR(50),
                         phone VARCHAR(20),
                         address VARCHAR(255),
                         signupDate DATETIME DEFAULT CURRENT_TIMESTAMP,
                         lastLoginDate DATETIME,
                         remainingTime INT DEFAULT 0,
                         PRIMARY KEY (memberId)
);

-- (이어서 CART 테이블 생성 쿼리 작성)
CREATE TABLE CART (
                      cartId INT AUTO_INCREMENT NOT NULL,
                      memberId VARCHAR(50) NOT NULL,
                      foodId VARCHAR(20) NOT NULL,
                      quantity INT NOT NULL,
                      PRIMARY KEY (cartId),
                      FOREIGN KEY (memberId) REFERENCES MEMBERS(memberId)
);

-- 4. 외래 키 체크 다시 켜기
SET FOREIGN_KEY_CHECKS = 1;

-- 5. 데이터 삽입
INSERT INTO MEMBERS (memberId, password, email, name, phone, address, remainingTime)
VALUES
    ('lee99', '1234', 'lee@naver.com', '이순신', '010-1234-5678', '대구시 중구', 120),
    ('gildong1', '5678', 'hong@gmail.com', '홍길동', '010-9876-5432', '서울시 종로구', 0),
    ('sk', 'sk', 'sksk@gmail.com', '김몰라', '010-1111-2222', '서울시 관악구', 60);