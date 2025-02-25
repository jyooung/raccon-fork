# 🦝배달하는 너구리🦝

## 프로젝트 소개

- 00의 민족, 0기요, 등의 음식 주문 관리 플랫폼 백엔드 개발
- 배달 및 포장 음식 주문 관리 플랫폼 백엔드 개발


## 프로젝트 목적/상세

- 문서 제공에 있어 프론트엔드 개발자가 원활하게 개발하여 협력 가능하도록 문서 작성
- 식당에서 음식 정보를 등록하고 사용자가 원하는 음식을 주문하고 할당된 라이더가 음식을 배달하는 과정을 배달 과정을 제외하여 동작하도록 구현
- RESTful API로 설계
- 유저, 음식점, 음식 등의 엔티티 간의 관계를 설정
- 결제 시스템, 주문 관리 시스템, 데이터 보존 및 삭제 처리, 접근 권한리, 배송지 정보 관리, AI API연동, 주문 확인 방식 등의 상세 기능을 구현


## 팀원 구성

**이동하**

- 주문, 결제 로직 담당

**전은배**

- AI연동, 찜, 카테고리. 리뷰, 리뷰이미지

**김도연**

- 음식점, 음식점 이미지, 이미지 저장 기능 모듈, 음식

**박지영**

- 음식, 이메일 인증 모듈, README작성


## 프로젝트 구조
```
│────── spartaproject
│           ├─ domain
│           │  ├─ QBaseEntity.java
│           │  ├─ QBaseTimeEntity.java
│           │  ├─ category
│           │  ├─ food
│           │  ├─ gemini
│           │  ├─ image
│           │  ├─ like
│           │  ├─ order
│           │  ├─ pay
│           │  ├─ review
│           │  ├─ store
│           │  └─ user
│           │     
│           └─ mappe         
├─ java
│  └─ com
│     └─ sparta
│        └─ spartaproject
│           ├─ SpartaProjectApplication.java
│           ├─ common
│           │  ├─ CacheConfig.java
│           │  ├─ CacheType.java
│           │  ├─ CustomAuditorAware.java
│           │  ├─ FileUtils.java
│           │  ├─ GeminiRestTemplateConfig.java
│           │  ├─ S3Config.java
│           │  ├─ pageable
│           │  └─ security
│           ├─ controller
│           ├─ domain
│           │  ├─ BaseEntity.java
│           │  ├─ BaseTimeEntity.java
│           │  ├─ CircularService.java
│           │  ├─ category
│           │  ├─ food
│           │  ├─ gemini
│           │  ├─ image
│           │  ├─ like
│           │  ├─ mail
│           │  ├─ order
│           │  ├─ pay
│           │  ├─ review
│           │  ├─ slack
│           │  ├─ store
│           │  ├─ user
│           │  └─ verify
│           ├─ dto
│           │  ├─ request
│           │  └─ response
│           ├─ exception
│           └─ mapper
└─ resources
      └─ db
         └─ migration
         └─ logback.xml
```

- **Monolithic Application**으로 개발
- **Layered Architecture계층 구조**
- **Entity 및 DTO**를 기능별로 분리하여 관리
- **RESTful API** 원칙에 따라 설계
- **Exception Handling**으로 글로벌 예외 처리 (ExceptionHandler 사용)

## 프로젝트 기능 및 실행 방법

### 사용자
- 회원가입 기능
- 로그인
- 아이디 찾기
- 사용자 정보 조회
- 사용자 비밀번호 변경
- 잠금 회원 활성화
- 매니저 계정 등록하기
- 마스터 계정 등록하기
- 이메일 인증

### 음식점

- 음식점 등록하기
- 음식점 전체, 상세, 카테고리별 조회
- 내 음식점 목록 조회하기
- 음식점 정보 수정하기
- 음식점 상태 변경하기
- 음식점 삭제하기

### 음식

- 음식 등록
- 음식 수정
- 음식 상태 변경
- 음식 숨김 상태 변경
- 음식 전체 조회, 음식점 별 음식 조회, 음식 상세조회
- 음식 삭제

### 주문

- 주문하기
- 주문 취소
- 주문 상태 변경
- 주문 상태 조회
- 주문 받기
- 주문 거절
- 주문 내역 조회, 주문 상세 조회

### 결제

- 결제하기
- 결제 상세 확인
- 결제 내역 리스트 확인
- 결제 정보 수정
- 결제 삭제(취소)

### 리뷰

- 리뷰 등록
- 리뷰 수정
- 리뷰 삭제
- 리뷰 전체 조회
- 음식점 별 리뷰 조회
- 내가 작성한 리뷰 조회
- 리뷰 상세 조회

### 찜

- 사용자용 → 찜 전체 조회
- 찜 상세 조회
- 찜 하기 → 토글방식
- 찜 삭제

### AI

- AI 질문 목록 전체 조회
- AI 질문 목록 상세 조회
- AI 질문 목록 생성
- AI 질문 목록 수정
- AI 질문 목록 삭제

### 실행 방법 (Build.gradle 참고하기)

- port: 8888사용
- JDK: 17버전 사용
- 의존성
    - Spring Boot 기본 (JPA, Vaildation)
    
    ```
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    ```
    
    - QueryDSL
    
    ```
    implementation "com.querydsl:querydsl-jpa:${querydslVersion}:jakarta"
    annotationProcessor "com.querydsl:querydsl-apt:${querydslVersion}:jakarta"
    annotationProcessor "jakarta.annotation:jakarta.annotation-api"
    annotationProcessor "jakarta.persistence:jakarta.persistence-api"
    ```
    
    - Lombok
    
    ```
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
    ```
    
    - PostgreSQL 데이터베이스 드라이버
    
    ```
    runtimeOnly 'org.postgresql:postgresql'
    ```
    
    - QueryDSL 관련 빌드 설정
    
    ```
    def querydslSrcDir = 'src/main/generated'
    clean {
        delete file(querydslSrcDir)
    }
    ```
    
    - 테스트 관련 의존성
    
    ```
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.springframework.security:spring-security-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
    ```
    
    - 테스트 설정 (JUnit 플랫폼 사용)
    
    ```
    tasks.named('test') {
        useJUnitPlatform()
    }
    ```
    
## 기술 스택

- **백엔드:** Spring Boot 3.x
- **데이터베이스:** PostgreSQL
- **빌드 툴:**  Gradle
- **버전 관리:** Git을 이용한 버전 관리 (GitHub, GitLab, Bitbucket 등)
- **배포**(deploy) : aws, docker, github action
- **API** : Gemini api, Slack Webhook

## 프로젝트 아키텍처

- ERD
    
    ![너구리_ERD.png](https://github.com/jyooung/raccon-fork/blob/dev/%EB%84%88%EA%B5%AC%EB%A6%AC_ERD.png)
    
- API docs
    
    링크 참고: [https://www.notion.so/teamsparta/API-19a2dc3ef514802bacbefdced00b1965](https://www.notion.so/19a2dc3ef514802bacbefdced00b1965?pvs=21)
