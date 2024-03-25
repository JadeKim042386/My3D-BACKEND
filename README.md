# My3D-BACKEND

- [My3D](https://github.com/JadeKim042386/My3D) BACKEND  
- 3D 모델을 공유하여 다운로드받을 수 있는 게시판 서비스
- Supported 3D Model Extension: *.stl, *.stp

## Development Environment

- Intellij IDEA Ultimate
- Java 17
- Gradle 8.5
- Spring Boot 2.7.18

## Project Structure

![](./imgs/my3d_backend_project_structure.svg)

## AWS Structure
![](./imgs/my3d_backend_aws_structure.svg)

## Tech Stack

| BackEnd                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| ![SpringBoot](https://img.shields.io/badge/SPRINGBOOT-6DB33F?style=for-the-badge&logo=springboot&logoColor=white) ![SpringSecurity](https://img.shields.io/badge/SPRINGSECURITY-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white) |
| ![SpringDataJpa](https://img.shields.io/badge/SPRING_DATA_JPA-6DB33F?style=for-the-badge) ![QueryDSL](https://img.shields.io/badge/QueryDSL-009DB8?style=for-the-badge) |
| ![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white) ![Mockito](https://img.shields.io/badge/Mockito-25A162?style=for-the-badge)|

| DevOps                                                                                                       |
|--------------------------------------------------------------------------------------------------------------|
| ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4479A1?style=for-the-badge&logo=postgresql&logoColor=white) |
| ![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white) ![EC2](https://img.shields.io/badge/Amazon%20EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white) ![S3](https://img.shields.io/badge/Amazon%20S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white) ![RDS](https://img.shields.io/badge/Amazon%20RDS-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white) |

## Features

- [X] 로그인/회원가입
  - [X] 비밀번호 찾기
  - [X] 이메일 인증
- [X] 게시글 작성/수정/삭제
- [X] 게시글에 첨부된 파일 다운로드
- [X] 댓글 & 대댓글 추가/삭제
- [X] 좋아요 추가/삭제
- [X] 좋아요, 작성일자 정렬
- [X] 제목 검색
- [X] 댓글 추가시 알람 전송
- [X] 유저 정보 수정
- [X] 회원 탈퇴
- [X] 구독 기능

📝 [요구사항](./docs/requirements.md)

## Flow Chart

1. [회원가입](#1-회원가입)
2. [로그인](#2-로그인)
3. [Authentication (인증)](#3-authentication-인증)
4. [알람](#4-알람)

### 1. 회원가입

```mermaid
  sequenceDiagram
    autonumber
    actor client
    client ->>+ 회원 유형 선택: 회원가입
    activate client
    alt 기업/기관일 경우
        회원 유형 선택 ->>+ 사업자 인증: 사업자 인증 페이지 요청
        사업자 인증 ->> 사업자 인증: 사업자 인증
        사업자 인증 ->>- 회원 정보 입력: 회원가입 페이지 요청
        activate 회원 정보 입력
    else 개인일 경우
        회원 유형 선택 ->> 회원 정보 입력: 회원가입 페이지 요청
    end
    회원 정보 입력 ->> 회원 정보 입력: 이메일 인증
    회원 정보 입력 ->>+ WAS: 회원가입 요청
    deactivate 회원 정보 입력
    WAS ->> WAS: validation
    WAS ->> DB: 유저 정보 저장
    WAS ->> WAS: JWT 토큰 생성
    WAS -->>- client: JWT 토큰 전달
    client ->> client: 홈페이지 redirect 
    deactivate client
```

### 2. 로그인

```mermaid
sequenceDiagram
    autonumber
    actor client
    client ->> WAS: 로그인 요청
    activate client
    WAS ->>+ DB: 유저 정보 요청
    activate WAS
    DB -->>- WAS: 유저 정보 반환
    WAS ->> WAS: 비밀번호 일치 확인
    WAS ->> WAS: JWT 토큰, Refresh Token 생성
    WAS -->>- client: Token 전달
    client ->> client: 홈페이지로 이동
    deactivate client
```

### 3. Authentication (인증)

```mermaid
sequenceDiagram
    autonumber
    actor client
    client ->> WAS: Authentication 요청
    activate client
    activate WAS
    WAS ->> WAS: JWT 토큰 확인
    alt JWT 토큰이 유효한 경우
        WAS ->> WAS: 토큰 정보로 UserDetail 객체 생성
        WAS ->> WAS: Authentication
        WAS -->> client: 인증 완료
    else JWT 토큰이 만료된 경우
        WAS ->> WAS: Refresh Token parsing & validation
        alt Refresh Token이 유효한 경우
            WAS ->> WAS: JWT 재발행
            WAS ->> WAS: Authentication
            WAS -->> client: 재발행된 JWT 토큰 전달
        else Refresh Token이 유효하지않은 경우
            WAS ->> client: 로그인 페이지 이동
        end
    else JWT 토큰이 유효하지않은 경우
        WAS -->> client: 로그인 페이지 이동
        deactivate WAS
        deactivate client
    end
```

### 4. 알람

#### 4.1. 페이지 전환
```mermaid
sequenceDiagram
    autonumber
    actor client
    client ->> WAS: GET /api/v1/alarm
    activate client
    activate WAS
    WAS ->>+ DB: 알람 정보 요청
    DB -->>- WAS: 알람 정보 반환
    WAS -->>- client: 알람 정보 전달
    deactivate client
```
#### 4.2. 이벤트 발생 (댓글 작성)
```mermaid
sequenceDiagram
    autonumber
    actor client
    activate client
    WAS ->>+ DB: 댓글 저장
    activate WAS
    WAS ->> DB: 알람 저장
    deactivate DB
    alt 알람 수신자가 로그인 상태인 경우
      WAS -->>- client: 알람 정보 전달 (SseEmitter, Websocket)
    end
    deactivate client
```

## ERD

- 이미지를 클릭하면 ERDCloud 페이지로 이동합니다.

[![ERD](./imgs/my3d-erd.png)](https://www.erdcloud.com/p/dTQwEsmpwMbRdEtbx)
