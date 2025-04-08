# 📈 한국투자증권 REST API 기반 주식 포트폴리오 조회 시스템

> 한국투자증권 Open API를 활용하여 사용자 계좌의 **잔고**, **체결 내역**, **현재가**, **주문 가능 금액**을 통합 조회하는 주식 포트폴리오 시스템입니다.

---

## 🛠 기술 스택

- Java (Servlet, JSP)
- Jackson 
- Resilience4j (RateLimiter)
- CompletableFuture (비동기 처리)
- JSTL 
- MariaDB + HikariCP (DB)

---

## 📌 주요 기능

| 기능                | 설명 |
|---------------------|------|
| 잔고 조회           | 사용자의 보유 주식 잔고 정보 확인 |
| 체결 내역 조회      | 종목별 체결 시각, 가격, 수량 등 확인 |
| 현재가 조회         | 실시간 주식 가격 정보 확인 |
| 주문 가능 정보 조회 | 종목별 매수 가능한 금액 및 수량 확인 |
| 통합 포트폴리오     | 위 기능들을 통합하여 비동기로 호출 및 반환 |
| 비동기 + RateLimiter | 초당 2회 제한 준수하며 API 호출 |

---

## 🧾 REST API 엔드포인트 매핑

| 기능                | 엔드포인트                                                    | HTTP 메서드 | 메서드 이름                  |
|---------------------|--------------------------------------------------------------|-------------|-----------------------------|
| 잔고 조회           | `/uapi/domestic-stock/v1/trading/inquire-balance`            | `GET`       | `getBalanceInquiry()`       |
| 체결 내역 조회      | `/uapi/domestic-stock/v1/quotations/inquire-ccnl`            | `GET`       | `getInquireCcnl(String)`    |
| 현재가 조회         | `/uapi/domestic-stock/v1/quotations/inquire-price`           | `GET`       | `getPriceInquiry(String)`   |
| 주문 가능 정보 조회 | `/uapi/domestic-stock/v1/trading/inquire-psbl-order`         | `GET`       | `getPsblOrder(String)`      |

---

