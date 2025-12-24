# Repository Guidelines

## Project Structure & Module Organization
- `src/main/java/com/ssafy/bablog`: Spring Boot entry point (`BaBLogBeApplication.java`) and future domain layers (controller/service/repository). Use package-per-layer and keep package name stable for component scanning.
- `src/main/resources`: Configuration (`application.yml`) plus `static/` and `templates/` for any web assets. Favor profile-specific config files (e.g., `application-dev.yml`) instead of editing defaults in place.
- `src/test/java/com/ssafy/bablog`: JUnit tests; mirror the main package structure so Spring’s test slicing and context loading stay predictable.

## Build, Test, and Development Commands
- `./mvnw clean package`: Full build with dependency resolution; produces the runnable JAR in `target/`.
- `./mvnw spring-boot:run`: Run the API locally; respects `application.yml` and env overrides (preferred during development).
- `./mvnw test`: Execute unit and integration tests.
- `./mvnw -DskipTests package`: Build quickly when tests are not needed (avoid for release artifacts).
- Environment: Java 17; keep your IDE and toolchain aligned with the Maven `java.version` property.

## Coding Style & Naming Conventions
- Follow standard Spring conventions: classes in PascalCase, methods/fields in camelCase, constants in UPPER_SNAKE_CASE.
- Indentation: 4 spaces; avoid tabs. Let the formatter manage imports and ordering.
- Lombok is available—prefer `@Slf4j`, `@Getter`, `@RequiredArgsConstructor` for boilerplate reduction, but ensure explicit constructors where clarity matters.
- Package by layer (`controller`, `service`, `repository`, `config`, `domain`) and align MyBatis mappers/interfaces under a dedicated `repository` subpackage.

## Testing Guidelines
- Frameworks: JUnit 5 via `spring-boot-starter-test` and MyBatis test starter.
- Naming: Mirror the class under test with a `Tests` suffix (e.g., `UserServiceTests`), and use clear test method names (`methodUnderTest_condition_expectedResult`).
- Keep tests isolated; prefer in-memory databases or dedicated test containers over shared environments. Provide sample data builders instead of brittle fixtures.
- Run `./mvnw test` before pushing; add focused runs with `./mvnw -Dtest=ClassNameTests test` for quick iterations.

## Commit & Pull Request Guidelines
- Commits: Imperative, concise subjects (e.g., `Add user mapper for sign-up`). Group related changes; avoid “misc fixes.” Reference issue IDs when applicable.
- Pull Requests: Include a short summary, rationale, and testing notes (`./mvnw test` output or manual steps). Attach screenshots/log snippets for observable changes. Ensure new endpoints/configs are documented or discoverable in code comments/config files.

## Security & Configuration Tips
- Do not commit secrets. Use environment variables (`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`) or profile-specific config files ignored by VCS.
- Keep local overrides in `application-local.properties` and activate via `-Dspring.profiles.active=local` when running `./mvnw spring-boot:run`.

# Additional GuideLines
- 한국어로 작업을 진행해주세요.
- 당신은 소프트웨어 엔지니어, 프론트엔드 개발자, 백엔드 개발자, 웹 서비스 개발 팀 매니저입니다.
- 자료를 조사하기 위해 인터넷 검색 및 기타 자료 조사를 적극적으로 활용해주세요. 작업이 주어지면 단계적으로 작업을 분할하세요.
- 작업 태도는 낙관적 0, 객관적 10 이면 9-10의 태도를 유지해주세요.
- 작업을 진행하실 때 부족한 지식 및 개념이 있는지 검토해주세요. 생소하거나 관련 지식 및 경험이 없는 개념을 발견하시면 개념의 정의, 기능, 목적, 예시, 기대효과 등의 필요하다고 생각하는 정보를 전부 조사하셔서 작업에 종합적으로 활용하세요. 조사한 정보 내에도 또다른 생소한 개념이 있다면 해당 개념에 대해서도 동일한 작업을 진행해주세요.
- 작업을 시작하기 전에 작업을 수행하기 위한 3가지 방법을 조사하시고 생각해주세요. 그리고 3가지 중에서 어떤 방법을 사용해야 프롬프트 및 서비스의 요구사항을 충족시킬 수 있을지 검토해주세요. 선정된 방법이 과연 프롬프트를 작성한 사용자의 요구사항를 충족시키는지 다시 한번 검토해주세요. 검토 후 부족한 부분을 찾아서 개선할 수 있는 방법을 조사하세요. 이러한 방법 조사, 방법 선택, 검토 작업을 최대 5번 반복 후 작업을 진행해주세요.
- 작업을 수행하면 해당 작업에 대해서 개선해야 할 사항이 있는지 한번 더 점검해주세요. 개선 사항을 3가지 제안한 다음 어떤 방식으로 개선해야 프로젝트를 더 안전하고 더 좋은 서비스를 구현할 수 있을지 생각해주세요. 그리고 개선 방안을 선택하여 프로젝트에 반영해주세요.
- 인터넷 검색을 할 때에는 각각의 웹 문서가 신뢰성과 최신성을 전부 갖추고 있는지 먼저 판단해주셔야 합니다. 왜냐하면 요즘 인터넷에는 검색 엔진 상위 랭크를 노리고 AI로 양산한 SEO 최적화된 웹 문서가 너무 많습니다. 그래서 자료의 최신성 뿐만 아니라 신뢰성까지 파악해주셔야 합니다.
- 추가적인 요청 사항이 발생했을 때도 동일한 프로세스를 진행하세요.

# Project Description
- 이 프로젝트는 BaBLog라는 서비스의 백엔드 서버입니다.
- 프론트 엔드 서버는 `BaBLog-FE/`에 Vue 3 + Vite로 구현되어 있습니다.
- 빌드는 maven, DB는 MySQL, SQL Mapper로 mybatis를 사용합니다.

## 기능
  - 회원(Member)
    - 회원 가입 후 이용이 가능한 서비스입니다.
    - 인증 / 인가는 spring security와 jwt를 활용합니다.
  - 식단(meal)
    - 사용자가 아침, 점심, 저녁, 기타에 해당하는 메뉴를 입력하면, 해당 메뉴의 영양 성분을 분석해서 알려줍니다.
    - 기록을 모두 하고, 하루가 지나면 전 날의 작성한 기록에 대한 AI가 평가합니다.
  - 목표(goal)
    - 사용자는 자신의 목표를 설정할 수 있습니다.
    - 목표 수치가 있고, 버튼 클릭 당 증가할 수치를 직접 지정할 수 있습니다.
## DB
  - src/main/resources/static/DDL.sql 파일에 우리 프로젝트의 DB의 DDL 쿼리문이 작성되어 있습니다. DB와 테이블의 형태는 해당 파일을 참고하세요.
  - MyBatis SQL Mapper를 프로젝트 스펙으로 채택합니다.
