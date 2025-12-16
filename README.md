# BaBLog-BE

## 프로젝트 개요
- Spring Boot 3.5.8, Java 17, MyBatis, MySQL 기반의 BaBLog 백엔드.
- 주요 도메인: 회원/인증(JWT), 식단/식품, 식단 로그, 목표 및 리포트(DDL 참고).
- 빌드/실행: `./mvnw clean package`, 로컬 실행: `./mvnw spring-boot:run`, 테스트: `./mvnw test`.

## 디렉터리 구조
- `src/main/java/com/ssafy/bablog`
  - `config`: 보안/CORS와 Swagger OpenAPI 설정.
  - `security`: JWT 토큰 필터, 발급기, 블랙리스트, `UserDetailsService`.
- `member`: 컨트롤러/DTO/서비스/리포지토리/도메인/매퍼.
- `meal`: 컨트롤러/DTO/서비스/스케줄러/리포지토리/도메인/매퍼/영양 계산기.
- `meal_log`: 도메인 및 리포지토리/매퍼.
- `food`: 식품 도메인 및 리포지토리/매퍼.
- `member_nutrient_daily`: 일자별 회원 영양 목표 스냅샷 저장(목표 변경 시 과거 기록 보호).
- `src/main/resources`
  - `application.yml`: DB(JDBC), MyBatis, JWT 설정. 민감 정보는 환경 변수(`SPRING_DATASOURCE_*`, `SECURITY_JWT_SECRET`)로 덮어쓰는 것을 권장.
  - `mapper/`: MyBatis XML 매퍼 (Member, Meal, MealFood, MealLog, Food).
  - `static/`: `DDL.sql`(DB 스키마), `member-api.json`(회원 API 예시).
  - `templates/`: 템플릿 디렉터리(현재 비어 있음).
- `src/test/java/com/ssafy/bablog`: `BaBLogBeApplicationTests` 기본 부트스트랩 테스트.
- `target/`: 빌드 산출물.

## 개발 메모
- `BaBLogBeApplication`에서 MyBatis 매퍼 스캔 및 스케줄링 활성화.
- `SecurityConfig`에서 JWT 필터 기반의 무상태 인증 및 CORS 허용(origin: 5173/8080).
- DB 스키마는 `static/DDL.sql`을 기준으로 MyBatis 매퍼 XML과 도메인 클래스가 정리되어 있음.

## 회원 영양 권장량 API (요약)
- `GET /members/nutrients`: 저장된 권장 섭취량 조회.
- `POST /members/nutrients/recalculate`: 현재 회원의 성별/키/체중으로 권장 섭취량 재계산 후 저장.
- `PATCH /members/nutrients`: 사용자가 직접 영양 성분 값을 수정(필드 부분 업데이트 가능).
