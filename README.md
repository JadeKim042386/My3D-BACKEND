# My3D-BACKEND

[My3D](https://github.com/JadeKim042386/My3D) Backend

## Development Environment

- Intellij IDEA Ultimate
- Java 17
- Gradle 8.5
- Spring Boot 2.7.18

## Features

- [ ] 회원가입
- [ ] 로그인
- [ ] 게시글 작성/수정/삭제
- [ ] 댓글 작성/삭제
  - [ ] 대댓글 작성/삭제
- [ ] 게시글에 대한 좋아요 추가/삭제
- [ ] 댓글 추가시 알람 전송
- [ ] 유저 정보 수정
- [ ] 구독 기능

## Flow Chart

1. [회원가입](#1-회원가입)
2. [로그인](#2-로그인)
3. [Authentication (인증)](#3-authentication-인증)

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
## ERD

- [ERD](./imgs/my3d-erd.png)