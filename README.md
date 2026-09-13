# Motorsport Community

국내의 모터스포츠팬들을 위한 경기 관련 소식을 공유할 수 있는 커뮤니티입니다.

회원 인증, 게시글, 댓글, 좋아요, 이미지 업로드 등의 커뮤니티 기능을 구현하고,
AWS 환경에 직접 배포하여 실제 서비스가 동작할 수 있도록 구성했습니다.

단순 기능 구현뿐만 아니라 **JPA 쿼리 최적화, 검색 성능 비교, CI/CD, AWS 인프라 구성**까지 경험하는 것을 목표로 진행했습니다.

---

##  프로젝트 소개
#### 서비스 컨셉

- 우선 설계를 하기에 앞서 어떤 커뮤니티를 만들고 있는지 그 컨셉을 잡는 것이 몰입에 좋겠다 생각

→ 모터스포츠(레이싱)이 나의 주된 관심사, 이 모터스포츠를 시청하는 사람들이 이용하는 그러한 커뮤니티를 만들고 싶다 생각

→ 우선 서비스는 한국을 대상

- 국내에 있는 가장 큰 F1, 모터스포츠 관련 네이버 카페를 기준점으로 잡음
- 관련 정보나 문답 같은 부분은 거의 다 이 카페에서 일어난다고 봐도 무방
  - All about F1 : 멤버수 2.6만&#x20;
    - 멤버 수는 적지만 로그인을 안해도 게시글을 볼 수 있었기에 굳이 안 한 사람들이 많을 것이라 생각
  - 2022.12(개설)\~2026&#x20;
    - 게시글 : 5.5만 개&#x20;
      - 3.5년 → 5.5만
      - 1년 → 1.6만
      - 1달 → 1300개
    - 방문자 : 866만 명&#x20;
      - 3.5년 → 866만
      - 1년 → 247.5만
      - 1달 →  20.6만 (회원 + 비회원)

        ⇒ MAU : 20.6만
  
  #### 1차적으로 얻은 결론
  - 같은 기간 (3.5년)
    - 게시글(Write) : 1300/월
    - 방문자(Read) : 20.6만 /월
    `⇒ 쓰기보다 읽기 트래픽이 훨씬 클 것으로 예상`
 
- MAU : 20.6만 예상
  - DAU(일간 방문 사용자 빈도)
  - F1등의 모터스포츠는 보통 1달에 2번정도 경기를 진행
    - 보통 한 번에 3일정도 소요 → 금(연습), 토(예선), 일(경기)
    - 카테고리별로 거의 겹치지 않고 돌아가며 레이스를 진행

      Ex)
      - 1주차 금토일 : F1
      - 2주차 금토일 : WEC
      - 3주차 금토일 : F1
      - 4주차 금토일 : NLS
      - 즉, 1개월에 12일정도 경기가 존재 → 트래픽 발생&#x20;
        - 단순 계산으로 DAU : 1.8만
  - RPS(초당 요청 수)
    ```json
    RPS = (요청 수) / (하루의 총 시간(초)
    =>
    RPS = 270000 / 86400 = 3.125 (평균)

    ```
    - 요청수 = DAU x 접속 횟수(api요청)
      - 접속은 경기전(정보얻기), 중(공유), 후(후기) 합쳐 15번정도 접속한다 가정(보통 테스트시 15\~20사이의 값을 지정하는 것을 확인)
      → 1.8만 x15  → 27만
  
  #### 2차적으로 얻은 결론
  - 예상 평균 RPS(초당 요청) : 3.125
  - 피크 시간(경기 중, 이벤트 발생) : 3.125 x 10&#x20;
    - 평균보다 10배정도 몰린다고 가정&#x20;
      - 찰리께서는 5\~6배 정도 말씀하셨지만, F1관련  커뮤니티라는 특수성(희귀)을 가정했을때 특성상 좀 더 몰릴 것 같았음
      - (실제 카페글 분석 → )
      - 평소 게시글 수 : 10\~15개 수준
      - 경기 당일  : 100\~110개 수준
  `⇒ 평균적으로 3.125에서 최대 31.25의 RPS예상`

  `→ 높아진 인기에 따른 트래픽 대비,더 높은 RPS대비 필요`



### 나의 커뮤니티 정리

- 내가 만들고자하는 서비스는 모터스포츠 전문 커뮤니티
- 이 커뮤니티의 예상 MAU는 20.6만명&#x20;
  - 정보 공유 커뮤니티의 특성 상 쓰기보다 읽기의 비율이 압도적
- 1달 중 12일에 트래픽이 집중 → DAU 1.8만명
- “평균 RPS 3.125, 피크 시간의 RPS 31.25”&#x20;
  - 새로고침 빈번한 “실시간성이 강한 커뮤니티”


### 주요 기능

* JWT 기반 회원 인증
* 회원가입 / 로그인 / 로그아웃
* Access Token / Refresh Token 관리
* 게시글 CRUD
* 댓글 CRUD
* 게시글 좋아요/조회수/검색
* 프로필, 게시글 이미지 업로드
* 검색기능
---

## 개발 인원 및 기간

* 개발 인원 : Backend,Frontend 1명
* 개발 기간 : `2026.05 ~ 2026.08`

---

# 기술 스택

* Spring Boot
* MySQL
* AWS
* Docker, Docker Hub
* Github Actions

---

#  Repository

### Backend

* [Backend GitHub](https://github.com/taejun-J/KTB_Project)

### Frontend

* [Frontend GitHub](https://github.com/100-hours-a-week/4-ayden-community-FE)


### 시연영상

* https://www.youtube.com/watch?v=QO0PeqBazdM

---

#  서버 아키텍처

서비스는 AWS 환경에 배포했습니다.

외부 사용자는 Route 53에 등록된 도메인을 통해 접근하며,
HTTPS 요청은 Application Load Balancer를 통해 Frontend와 Backend 서버로 전달됩니다.

```text
                         User
                           │
                           ▼
                       Route 53
                           │
                           ▼
                          ACM(Https)
                           │
                           ▼
                          ALB
                     ┌─────┴─────┐
                     │           │
                     ▼           ▼
                Frontend EC2   Backend EC2
                                   │
                          ┌────────┼────────┐
                          │        │        │
                          ▼        ▼        ▼
                        MySQL      S3   Elasticsearch
```

### 주요 설계 방향

* 외부 요청의 단일 진입점으로 ALB 사용
* Route 53을 이용한 도메인 연결
* ACM을 이용한 HTTPS 인증서 적용
* Frontend / Backend 서버 분리
  * private subnet 사용 및 bastion host를 통한 외부 인터넷 연결 설정
* MySQL을 애플리케이션 서버와 분리(별도의 EC2)
* S3를 이용한 이미지 파일 관리
* Docker 기반 애플리케이션 배포
* GitHub Actions를 이용한 CI/CD 구성

---

#  배포 구성

애플리케이션은 Docker Image로 빌드하여 EC2에서 Container 형태로 실행합니다.
배포 과정은 GitHub Actions를 통해 자동화했습니다.

```text
GitHub Push
     │
     ▼
GitHub Actions
     │
     ├── Build
     │
     ├── Test
     │
     ├── Docker Image Build
     │
     ▼
Docker Hub
     │
     ▼
Bastion EC2
     │
     ▼
Backend/Frontend EC2
     │
     ▼
Docker Container
```

## 배포 관련 설정 파일

실제 배포에 사용한 설정 파일은 Repository에 함께 관리합니다.

```text
.github/
└── workflows/
    ├── ci.yml
    └── ci-cd.yml

Dockerfile

src/main/resources/
├── application.yml
└── application-prod.yml
```

> 비밀번호, JWT Secret, AWS Access Key와 같은 민감 정보 => GitHub Secrets , BE EC2 .env로 관리합니다.

---
## GitHub Actions Runner IP 기반 동적 Security Group 제어

GitHub-hosted Runner는 실행할 때마다 Public IP가 변경될 수 있기 때문에,
Bastion 서버의 SSH 포트(22)를 특정 고정 IP로 제한하기 어려운 문제가 있었습니다.

이를 해결하기 위해 배포 Workflow 실행 시 다음 과정을 자동화했습니다.

```text
GitHub Actions Runner
        │
        │ OIDC 인증
        ▼
     AWS IAM Role
        │
        ▼
Runner Public IP 확인
        │
        ▼
Bastion Security Group
TCP 22 / Runner IP(/32) 임시 허용
        │
        ▼
Runner → Bastion → Backend EC2
            SSH / ProxyJump
        │
        ▼
       Deploy
        │
        ▼
Security Group Rule 제거

#  프로젝트 구조

애플리케이션은 다음과 같은 계층 구조로 구성했습니다.

```text
Client
   │
   ▼
Controller
   │
   ▼
Service
   │
   ▼
Repository
   │
   ▼
Database
```

각 계층의 책임을 분리하여 Controller가 비즈니스 로직을 직접 처리하지 않도록 구성했습니다.

---

#  주요 구현 기능

##  User

### 회원가입

* 이메일
* 비밀번호
* 비밀번호 확인
* 닉네임
* 프로필 이미지

정보를 기반으로 회원을 생성합니다.

Bean Validation을 사용하여 Request DTO 단계에서 입력값을 검증합니다.


### 로그인

로그인 성공 시 다음 두 종류의 JWT를 발급합니다.

* Access Token
* Refresh Token

Access Token은 API 인증에 사용하며,
Refresh Token은 Access Token 재발급에 사용합니다.

### JWT 인증

인증이 필요한 API 요청은 JWT Authentication Filter를 통과합니다.
반대로 인증이 필요없는(단순조회, 회원가입, 로그인 api는 White Filter로 검증에서 제외)
```text
Request
   │
   ▼
JwtAuthenticationFilter
   │
   ├── Token 검증 실패 → 인증 오류
   │
   └── Token 검증 성공
             │
             ▼
        Controller
```

JWT에서 추출한 사용자 ID는 Controller에서 사용할 수 있도록 Request Attribute에 저장합니다.

---

#  Posts

게시글에 대해 다음 기능을 제공합니다.

* 게시글 CRUD
* 게시글 리스트 조회
* 게시글 상세 조회
* 게시글 조회수 측정
* 게시글 검색
* 게시글 이미지 업로드

게시글 삭제는 실제 DB Row를 제거하는 대신 상태값을 변경하는 **Soft Delete** 방식으로 구현했습니다.
-> 커뮤니티 서비스는 유저가 삭제 후 복구할 수도 있기때문

```text
ACTIVE
  │
  ▼
INACTIVE / DELETED
```

이를 통해 데이터 삭제 이후에도 필요에 따라 기록을 관리할 수 있도록 구성했습니다.

---

#  Comments

댓글에 대해 다음 기능을 구현했습니다.

* 댓글 CRUD

댓글은 게시글과 사용자 정보를 참조합니다.

```text
User
  │
  │ 1
  │
  │ N
Comment
  │
  │ N
  │
  │ 1
Post
```

---

#  Post Like

사용자는 게시글에 좋아요를 추가하거나 취소할 수 있습니다.

같은 사용자가 같은 게시글에 여러 번 좋아요를 등록하지 못하도록
`user_id`, `post_id` 조합에 Unique Constraint를 적용했습니다.

```text
UserPostLike

user_id
post_id

UNIQUE(user_id, post_id)
```

따라서 애플리케이션 로직뿐만 아니라 Database 레벨에서도 중복 좋아요를 방지합니다.

---

#  이미지 업로드

프로필 이미지와 게시글 첨부 이미지는 AWS S3에 저장합니다.

```text
Client
   │
   ▼
Spring Boot
   │
   ▼
S3
```

DB에 이미지 바이너리 자체를 저장하지 않고 이미지의 위치를 저장하는 방식으로 구성했습니다.


# 🔎 게시글 검색 성능 개선

게시글 검색 기능을 구현하면서 데이터 증가에 따른 검색 성능 차이를 확인하기 위해 다음 세 가지 방식을 비교했습니다.

1. MySQL LIKE
2. MySQL FULLTEXT INDEX
3. Elasticsearch

약 **100,000건의 게시글 더미 데이터**를 생성하여 테스트했습니다.

## LIKE 검색

```sql
WHERE post_name LIKE '%spring%'
```

일반 B-Tree Index를 생성해도 문자열 앞에 `%`가 존재하는 검색에서는 Index를 효율적으로 사용할 수 없었습니다.

`EXPLAIN ANALYZE` 결과 Full Table Scan이 발생하는 것을 확인했습니다.

---

## FULLTEXT INDEX

MySQL FULLTEXT INDEX를 추가하여 검색했습니다.

```sql
MATCH(post_name)
AGAINST('spring' IN NATURAL LANGUAGE MODE)
```

LIKE 검색보다 검색 범위를 효율적으로 줄일 수 있었으며 성능이 개선되는 것을 확인했습니다.

---

## Elasticsearch

게시글 데이터를 Elasticsearch에 색인한 후 `match query`를 이용해 검색했습니다.

```text
MySQL
  │
  │ Indexing
  ▼
Elasticsearch
  │
  ▼
Search
```

테스트 데이터 기준 검색 성능은 다음 순서로 확인했습니다.

```text
Elasticsearch
      ↓
MySQL FULLTEXT
      ↓
MySQL LIKE
```

이를 통해 단순히 Database Index를 추가한다고 항상 검색 성능이 개선되는 것이 아니라,
**검색 조건과 데이터 특성에 적합한 Index 및 검색 시스템을 선택해야 한다는 점**을 확인했습니다.

---

#  Health Check

로드 밸런서에서 Backend 서버의 정상 동작 여부를 확인할 수 있도록
Spring Boot Actuator를 이용한 Health Check API를 구성했습니다.

```text
ALB
 │
 │ Health Check
 ▼
Spring Boot
 │
 ▼
Actuator Health Endpoint
```

DB 상태도 Readiness 판단에 포함할 수 있도록 구성했습니다.

이를 통해 애플리케이션이 실행 중인 것뿐만 아니라 실제 요청을 처리할 준비가 되어 있는지를 확인할 수 있도록 했습니다.


#  트러블 슈팅

## 1. JPA N+1 및 조회 쿼리 최적화

게시글 목록 조회 과정에서 게시글별 사용자, 댓글 개수, 좋아요 개수 등을 조회하면서 불필요한 추가 Query가 발생할 가능성이 있었습니다.

Entity 자체를 반복 조회하는 방식 대신 JPQL DTO Projection과 Aggregate Query를 이용하여 필요한 데이터만 조회하도록 개선했습니다.

이를 통해 JPA의 객체 탐색 과정에서 발생할 수 있는 불필요한 Lazy Loading을 줄였습니다.

---

## 2. LIKE 검색에서 B-Tree Index가 사용되지 않는 문제

게시글 제목 검색 성능 개선을 위해 B-Tree Index를 생성했지만,

```sql
LIKE '%keyword%'
```

형태에서는 Index가 사용되지 않았습니다.

`EXPLAIN ANALYZE`를 통해 실행 계획을 분석한 결과 Full Table Scan이 발생하는 것을 확인했습니다.

이후 MySQL FULLTEXT와 Elasticsearch를 적용해 검색 성능을 비교했습니다.

이를 통해 **Index의 존재 여부보다 Query의 형태와 Index 구조의 관계가 중요하다**는 점을 확인했습니다.

---

## 3. ALB Health Check 실패

Backend EC2를 ALB Target Group에 연결하는 과정에서 Health Check 실패 문제가 발생했습니다.

일반 서비스 API 대신 Spring Boot Actuator의 Health Endpoint를 Health Check 전용 경로로 구성했습니다.

```text
ALB
 ↓
Health Check
 ↓
Spring Boot Actuator
 ↓
Application / DB 상태 확인
```

서비스 로직과 Health Check를 분리하여 보다 명확하게 서버 상태를 판단할 수 있도록 개선했습니다.

---

## 4. Docker 기반 배포 자동화

초기에는 서버에 직접 접속하여 애플리케이션을 빌드하고 실행해야 했습니다.

이후 다음 배포 과정으로 개선했습니다.

```text
Code Push
   ↓
GitHub Actions
   ↓
Test
   ↓
Docker Build
   ↓
Docker Image
   ↓
EC2 Deploy
```

애플리케이션 실행 환경을 Docker Image로 통일하여 로컬과 서버의 환경 차이를 줄이고 반복적인 배포 과정을 자동화했습니다.

---

#  프로젝트를 통해 학습한 내용

이 프로젝트를 통해 단순히 Spring Boot API를 구현하는 것을 넘어
애플리케이션이 실제 운영 환경에서 동작하기 위해 필요한 전체 과정을 경험했습니다.

특히 단순히 기능이 동작하는 것에서 끝내지 않고,
> **왜 이런 구조를 사용하는지,
> 현재 구조에서 어떤 문제가 발생할 수 있는지,
> 이를 어떻게 검증하고 개선할 수 있는지**
를 직접 확인하는 것을 프로젝트의 주요 목표로 삼았습니다.

