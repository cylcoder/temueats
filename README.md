# 🍔 TEMUEATS

## 📅 프로젝트 진행 기간

2024.11.06 - 2024.11.18

## ❓ 프로젝트 목적

- 기획자, 웹디자이너, 프론트 엔지니어의 기능/비기능적인 요구사항을 구체화하여 모놀리식 프로젝트로 구현
- 대용량 처리 프로젝트 이전에 기본 기능을 구현해보며 기초 학습을 진행함

---

## 🗂️ 주요 서비스

- User
- Store
- Cart
- Order
- Payment
- Review
- Report

## 🧑🏻‍💻 팀 구성

<table>
<tbody>
<tr>
<td align="center">
<a href="https://github.com/cylcoder">
<img src="docs/images/changyeon.jpg" width="100px;" alt="프로필이미지"/>
<br />
<sub><b>[팀장] 이창연</b></sub>
<br />
</a>
<span>가게, 메뉴, AI 추천</span>
</td>
<td align="center">
<a href="https://github.com/le-monaaa">
<img src="docs/images/yubin.jpeg" width="100px;" alt="프로필이미지"/>
<br />
<sub><b>이유빈</b></sub>
<br />
</a>
<span>유저, 즐겨찾기, 쿠폰</span>
</td>
<td align="center">
<a href="https://github.com/drinkgalaxy">
<img src="docs/images/hyunjin.jpg" width="100px;" alt="프로필이미지"/>
<br />
<sub><b>이현진</b></sub>
</a>
<br />
<span>주문, 결제, 장바구니</span>
</td>
<td align="center">
<a href="https://github.com/jeendale">
<img src="docs/images/hyungu.jpg" width="100px;" alt="프로필이미지"/>
<br />
<sub><b>지현구</b></sub>
<br />
</a>
<span>리뷰, 평점, 신고</span>
</td>
</tr>
</tbody>
</table>

## 📄 API docs

[API명세서](https://www.notion.so/6547132e93e440dc8adb9986a37e9f50?pvs=21)

![screencapture-localhost-8080-swagger-ui-index-html-2024-11-18-14_16_22](https://github.com/user-attachments/assets/b98d582a-2019-49cb-8200-4de4e8cd9c4c)

## 🛠 개발 환경

![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge&logo=hibernate&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)
![Kakao API](https://img.shields.io/badge/Kakao%20API-FFCD00?style=for-the-badge&logo=kakao&logoColor=black)
![Google Gemini AI](https://img.shields.io/badge/Google%20Gemini%20AI-4285F4?style=for-the-badge&logo=google&logoColor=white)
![AWS S3](https://img.shields.io/badge/AWS%20S3-569A31?style=for-the-badge&logo=amazonaws&logoColor=white)
![AWS EC2](https://img.shields.io/badge/AWS%20EC2-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white)

## 📃 시스템 아키텍처


## 📑 ERD

![team15-erd.png](docs%2Fimages%2Fteam15-erd.png)

## 📁 프로젝트 파일 구조

- Monolithic Application
- Layered Architecture

```
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── sparta
    │   │           └── temueats
    │   │               ├── TemueatsApplication.java
    │   │               ├── cart
    │   │               │   ├── controller
    │   │               │   │   └── CartController.java
    │   │               │   ├── dto
    │   │               │   │   ├── CartGetListResponseDto.java
    │   │               │   │   ├── CartUpdateRequestDto.java
    │   │               │   │   └── CartUpdateResponseDto.java
    │   │               │   ├── entity
    │   │               │   │   └── P_cart.java
    │   │               │   ├── repository
    │   │               │   │   └── CartRepository.java
    │   │               │   └── service
    │   │               │       └── CartService.java
    │   │               ├── coupon
    │   │               │   ├── controller
    │   │               │   ├── dto
    │   │               │   ├── entity
    │   │               │   ├── repository
    │   │               │   └── service
    │   │               ├── global
    │   │               │   ├── BaseEntity.java
    │   │               │   ├── ResponseDto.java
    │   │               │   └── ex
    │   │               │       ├── CustomApiException.java
    │   │               │       ├── entity
    │   │               │       │   └── P_error.java
    │   │               │       ├── handler
    │   │               │       │   ├── CustomExceptionHandler.java
    │   │               │       │   └── GlobalExceptionHandler.java
    │   │               │       └── repository
    │   │               │           └── ErrorRepository.java
    │   │               ├── menu
    │   │               │   ├── config
    │   │               │   ├── controller
    │   │               │   ├── dto
    │   │               │   ├── entity
    │   │               │   ├── repository
    │   │               │   └── service
    │   │               ├── order
    │   │               │   ├── controller
    │   │               │   ├── dto
    │   │               │   ├── entity
    │   │               │   ├── repository
    │   │               │   └── service
    │   │               ├── payment
    │   │               │   ├── controller
    │   │               │   ├── dto
    │   │               │   ├── entity
    │   │               │   ├── repository
    │   │               │   └── service
    │   │               ├── rating
    │   │               │   ├── controller
    │   │               │   ├── dto
    │   │               │   │   └── response
    │   │               │   ├── entity
    │   │               │   ├── repository
    │   │               │   └── service
    │   │               ├── report
    │   │               │   ├── controller
    │   │               │   ├── dto
    │   │               │   │   ├── request
    │   │               │   │   └── response
    │   │               │   ├── entity
    │   │               │   ├── repository
    │   │               │   └── service
    │   │               ├── review
    │   │               │   ├── controller
    │   │               │   ├── dto
    │   │               │   │   ├── request
    │   │               │   │   └── response
    │   │               │   ├── entity
    │   │               │   ├── repository
    │   │               │   └── service
    │   │               ├── s3
    │   │               │   ├── config
    │   │               │   ├── controller
    │   │               │   └── service
    │   │               ├── security
    │   │               │   ├── Controller
    │   │               │   ├── UserDetailsImpl.java
    │   │               │   ├── UserDetailsServiceImpl.java
    │   │               │   ├── config
    │   │               │   ├── filter
    │   │               │   └── util
    │   │               │       └── JwtUtil.java
    │   │               ├── store
    │   │               │   ├── controller
    │   │               │   ├── dto
    │   │               │   ├── entity
    │   │               │   ├── repository
    │   │               │   ├── service
    │   │               │   └── util
    │   │               │       ├── AuthUtils.java
    │   │               │       ├── GeoUtils.java
    │   │               │       └── ValidUtils.java
    │   │               └── user
    │   │                   ├── config
    │   │                   ├── controller
    │   │                   ├── dto
    │   │                   ├── entity
    │   │                   ├── repository
    │   │                   └── service
    │   └── resources
    │       └── application.yml
    └── test
        └── java
            └── com
                └── sparta
                    └── temueats
                        ├── TemueatsApplicationTests.java
                        ├── cart
                        │   └── service
                        ├── coupon
                        │   └── service
                        ├── menu
                        │   └── service
                        ├── order
                        │   └── service
                        ├── payment
                        │   └── service
                        ├── review
                        │   └── service
                        ├── store
                        │   └── service
                        └── user
                            └── service

```

## ⚙️ 실행 방법

**Required**
- Java 17 이상
- Git
- Gradle
- PostgreSQL
    - [PostgreSQL](https://www.postgresql.org/download/)
    - 실행 및 데이터베이스 생성
    ```bash
    - psql postgres
    - CREATE DATABASE temueats;
    ```
    - PostGIS
    ```bash
    - CREATE EXTENSION IF NOT EXISTS postgis; 
    ```
1. properties.yml 설정
    ```yml
    spring:
        application:
          name: temueats
    
    datasource:
        url: jdbc:postgresql://localhost:5432/temueats
        username: "your-username"
        password: "your-password"
    
        driver-class-name: org.postgresql.Driver
        hikari:
            maximum-pool-size: 10
            connection-timeout: 30000
            idle-timeout: 600000
            max-lifetime: 1800000
    
    jpa:
        hibernate:
          ddl-auto: update
        show-sql: false
        properties:
            hibernate:
              format_sql: false
        database-platform: org.hibernate.dialect.PostgreSQLDialect
    
    jwt:
        secret:
          key: 7KCA64qU7Jik64qY7JWE66mU66as7Lm064W47JmA7LSI7L2U652865a866W866i57JeI7Iq164uI64uk7ZaJ67O17ZW07JqU
    
    kakao:
        api:
          key: 644ac6096d5a2d9b7df7289e65d18985
   cloud:
    aws:
        s3:
          bucket: "your-bucket-name"
        credentials:
            access-key: "your-access-key"
            secret-key: "your-secret-key"
        region:
          static: "us-west-2"
    
    ai:
        api:
          url: "your-ai-api-url"
    ```

2. build
  - Mac
    ```bash
    ./gradlew build
    ```
  - Windows
    ```bash
    gradlew build
    ```

3. 실행
    ```bash
    java -jar build/libs/temueats-0.0.1-SNAPSHOT.jar
    ```
