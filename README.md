<img width="1200" height="600" alt="morflax-studio-2026-06-05 10 16 35-Photoroom" src="https://github.com/user-attachments/assets/6eb5d04f-b91a-45e8-98cb-71848033c41d" />

# 🖥️ PC방 키오스크 시스템 (KB_260522_KIOSK)

> **콘솔 인터페이스와 PC방 감성 테마 GUI 터미널을 결합한 통합 PC방 키오스크 솔루션**  
> 기존 파일 기반 데이터 저장 방식에서 JDBC 데이터베이스(MariaDB/MySQL) 구조로 성공적으로 전환 및 고도화한 프로젝트입니다.

---

## 📝 프로젝트 소개

본 프로젝트는 실제 PC방의 이용 흐름(회원가입/로그인 ➔ 시간 충전 ➔ 음식 주문 및 장바구니 ➔ 주문 내역 관리 및 사용 정보 연동)을 완벽하게 재현한 키오스크 프로그램입니다.  
사용자의 편리한 콘솔 인터페이스(CLI) 환경과 함께 실제 PC방 좌석을 모티브로 한 Swing 기반 터미널 GUI(`PcRoomTerminalFrame`)를 통해 시각적 몰입감과 안정성을 동시에 제공합니다.

---

## 👥 팀원 및 역할 (Git 기반 기여 상세)

Git 브랜치(`kmj`, `ljh`, `lsy`, `pjw`, `ydj`)와 커밋 로그를 바탕으로 정리한 팀원 간의 구체적이고 명확한 역할 분담 내역입니다.

### 👨‍💼 육동주 (ydj) - 팀장, DB 연결 & 시간 관리

- **인프라 및 환경 설정**: 프로젝트 최초 아키텍처 구성 및 JDBC 데이터베이스 연결 초기 세팅 (`ca47308 DB연결 초기 세팅`).
- **시간 관리 및 충전 시스템**: 회원 보유 시간 충전 로직 구현 및 남은 시간 실시간 갱신 연동.
- **예외 처리 및 정합성 보장**: 장바구니 수량 0개 시 발생하는 연동 예외 및 주문 프로세스 이후 유실되는 시간 데이터 정합성 이슈 해결.

### 👨‍💻 김민준 (kmj) - GUI 터미널, 입력 보안 & 카테고리 보조

- **터미널 GUI 프레임**: PC방 UX 감성의 Swing 래퍼 윈도우(`PcRoomTerminalFrame`) 추가 및 레이아웃 수정.
- **입력 보안 및 예외 방어**: 사용자 오입력을 방지하기 위한 키보드 방어 로직 추가 및 비정상 종료 버그 수정.
- **데이터 관리 보조**: 회원 정보 데이터 일부 연동 및 관리, 음식 카테고리 관련 표기법 통일 및 리팩토링.

### 👨‍🔧 이석윤 (lsy) - 주문 내역 & 데이터 모델 설계

- **주문 데이터 설계**: `orders.json` 및 `order_details.json`으로 스키마를 분리하여 주문 내역의 상세 관리 설계.
- **주문 영속성 및 조회 시스템**: 주문 확정 시 주문 내역을 저장하고 영구 조회하기 위한 핵심 로직 담당 (`LsyOrderHistoryCommand`, `JdbcOrderDaoImpl`).
- **UX 개선 및 리팩토링**: 주문 내역 조회 및 메인 메뉴에서 0번을 입력하면 직관적으로 되돌아가도록 제어 흐름 수정 및 통일.

### 🙋‍♂️ 박준우 (pjw) - 회원 정보 & 사용자 인증

- **회원 인증 시스템 구축**: 회원가입, 로그인, 로그아웃의 흐름 제어 및 상태 관리 패턴 구현 (`SignupCommand`, `LoginCommand`, `LogoutCommand`).
- **JDBC Member DAO 마이그레이션**: 회원 데이터를 JSON 파일 구조에서 JDBC 데이터베이스 연동 방식으로 완전 전환 및 고도화 (`JdbcMemberDaoImpl`).
- **권한 제어**: 로그인 및 비로그인 상태에 따른 명령어 실행 권한 분리 구현 (`LoginCommand`, `SignupCommand` 격리).

### 👨‍🍳 이재혁 (ljh) - 음식, 장바구니 & 음식 주문

- **메뉴 조회 및 카테고리 구성**: 음식 카테고리 매핑 및 메뉴 출력 로직 담당.
- **장바구니 시스템 주도**: 메뉴 선택, 장바구니 담기, 수량 변경 및 최종 주문 처리를 위한 코어 클래스 구현 (`CartCommand`, `FoodOrderController`, `FoodOrderService`).
- **장바구니 JDBC 마이그레이션**: 로컬 JSON 기반 장바구니 저장 방식을 데이터베이스 구조로 마이그레이션 및 JDBC DAO 통합 구현 (`JdbcCartDao`).

---

## 🛠 기술 스택 (Tech Stack)

- **Language**: Java 17
- **Database**: MariaDB / MySQL (JDBC 연결)
- **UI Framework**: Java Swing (GUI 래퍼 프레임), Console CLI
- **Build Tool**: Gradle
