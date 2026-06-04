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

INSERT INTO MEMBERS (memberId,
                     password,
                     email,
                     name,
                     phone,
                     address,
                     remainingTime) VALUES ('lee99', '1234', 'lee@naver.com', '이순신', '010-1234-5678', '대구시 중구', 120),
                                           ('gildong1', '5678', 'hong@gmail.com', '홍길동', '010-9876-5432', '서울시 종로구', 0);
