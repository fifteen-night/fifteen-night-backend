# fifteen-night-backend

![image](https://github.com/user-attachments/assets/ec808dce-6086-429b-b7e1-0707f1eb7d47)

# 목차

- 프로젝트 소개
- 서비스 구성 및 실행방법
- 사용한 기술 스택
- 설계 산출물
- 트러블 슈팅
- 프로젝트 팀원

## 프로젝트 소개

> 물류 관리 및 배송 시스템을 위한 MSA 기반 플랫폼 개발

본 프로젝트는 기업 간 거래(B2B) 환경을 위한 물류 관리 및 배송 시스템을 개발하는 것이 목표입니다. 실무에서 사용되는 구조를 모델링하여, 지역 허브 센터 기반의 상품 보관 및 이동, 배송 요청 및 경로 관리, 업체와의 연동, 배송 담당자 시스템을 포함한 복합적인 로직을 구현하였습니다.
MSA(Microservices Architecture) 구조를 채택하여, 각 도메인마다 별도 서비스를 구축하고 이를 Spring Cloud Gateway를 통해 통합하였습니다. 이를 통해 서비스 간의 책임을 명확히 분리하고, 유연한 확장성과 유지보수성을 확보할 수 있었습니다.

### 각 도메인별 CRUD + Search 구현 및 독립적인 서비스 운영(MSA)

- 회원가입, 로그인 - Gateway에서 각 서버로 인증 정보 전달
- 주문 →  배송 생성
- 거리에 따른 배송 루트 생성
- 배송루트별로 배송담당자 배정
- 출발 허브 배송담당자에게 최소발송시한 AI답변 슬랙 메세지 전송
- 슬랙을 통한 유저 간 커뮤니케이션

<br />

## ⚙ 기술 스택

### Back-end

<div>
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Java.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/SpringBoot.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/SpringSecurity.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/SpringDataJPA.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Qeurydsl.png?raw=true" width="80">
</div>

### Infra

<div>
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Docker.png?raw=true" width="80">

</div>

### Tools

<div>
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Github.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Notion.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Swagger.png?raw=true" width="80">
</div>

<br />

## 설계 산출물

## 테이블 명세서

👉🏻 [테이블 명세서 바로보기](https://www.notion.so/API-1b32dc3ef514809d9a04ddb618bd021e?pvs=21)

## ERD

![image](https://github.com/user-attachments/assets/0c019516-e78d-4108-9e3a-a2545eb8ce51)

## 프로젝트 아키텍쳐

![image](https://github.com/user-attachments/assets/091e6603-d3da-4bb8-9ec6-8b227a7e8302)

## 프로젝트 APIs

👉🏻 [API 바로보기](https://www.notion.so/API-1b32dc3ef514809d9a04ddb618bd021e?pvs=21)

<br />

## 프로젝트 팀원

## 👨‍💻 프로젝트 팀원

| Backend | Backend | Backend | Backend |
| --- | --- | --- | --- |
| <img src="https://avatars.githubusercontent.com/u/94097685?v=4" width="100"/> | <img src="https://avatars.githubusercontent.com/u/127301222?v=4" width="100"/> | <img src="https://avatars.githubusercontent.com/u/100333239?v=4" width="100"/> | <img src="https://avatars.githubusercontent.com/u/86669962?v=4" width="100"/> |
| [김재현](https://github.com/iconew123) | [서태웅](https://github.com/STW5) | [강성준](https://github.com/Goldbar97) | [한지해](https://github.com/hanjihae) |

## 팀원 역할

| 이름 | 역할 |
| --- | --- |
| **김재현** | 배송 서비스 관리, 배송 경로 구현 |
| **서태웅** | Gateway 구성, 사용자/배송담당자/슬랙 서비스 |
| **강성준** | 허브/허브간 이동정보, 허브 상품 재고 관리 |
| **한지해** | 상품, 주문, 업체 서비스, Gemini AI 연동 |
