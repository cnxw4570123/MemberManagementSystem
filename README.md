# 🛡️ 회원 관리 시스템

간단한 JWT 기반 회원 관리 시스템입니다.  
회원가입, 로그인, 관리자 권한 부여, API 문서화(Swagger), AWS EC2 배포
(JPA, RDB, 파일 사용 없이 **순수 메모리 관리**)

---

## 📌 프로젝트 개요

- **JWT** 기반 로그인 및 권한 인증 시스템 데모
- **일반 사용자(User), 관리자(Admin) 역할 분리**
- **API 문서화(Swagger/OpenAPI) 지원**
- **테스트 코드(JUnit, WebMvcTest) 작성**
- **EC2에 배포 및 외부 접속 가능**

---

## 🚦 주요 기능

- 회원가입 / 로그인
- JWT 토큰 발급 및 인증
- 관리자 권한 부여(권한 변경 API)
- Role 별 접근제한(관리자 전용 API)
- 올바른/잘못된 입력 테스트 케이스
- Swagger UI를 통한 API 테스트

---

## 📒 API 명세

### 1. 회원가입

- **POST** `/auth/sign-up`
- **Body**
  ```json
  {
    "username": "testuser",
    "password": "yourpassword",
    "nickname": "닉네임"
  }

- **Response** <br>
  성공(201)
  ```json
  {
    "status": "CREATED",
    "data": {
        "username": "JIN H2",
        "nickname": "Mentos",
        "role": "USER"
    }
  }
  ```

  실패(400)
  ```json
  {
    "status": "BAD_REQUEST",
    "code": "USER_ALREADY_EXISTS",
    "message": "이미 존재하는 아이디입니다."
  }
  ```

### 2. 로그인
- **POST** `/auth/sign-in`
- **Body** <br>
  로그인 성공(200)
  ```json
  {
    "status": "OK",
    "data": {
        "accessToken": "ey..."
    }
  }
  ```

  로그인 실패(401)
  ```json
  {
    "status": "BAD_REQUEST",
    "code": "INVALID_CREDENTIALS",
    "message": "아이디 또는 비밀번호가 일치하지 않습니다."
  }
  ```

### 3. 관리자 권한 부여
- **PATCH** `/admin/users/{userId}/roles`
  - **Header** `Authorization : Bearer <토큰>`
- **Body** <br>
  관리자 권한 부여 성공(202)
  ```json
  {
    "status": "ACCEPTED",
    "data": {
        "username": "JIN H2",
        "nickname": "Mentos",
        "role": "ADMIN"
    }
  }
  ```

  관리자 권한 부여 실패(404) - 존재하지 않는 유저
  ```json
  {
    "status": "NOT_FOUND",
    "code": "USER_NOT_FOUND",
    "message": "사용자를 찾을 수 없습니다."
  }
  ```

  관리자 권한 부여 실패(403) - 권한 부족
  ```json
  {
    "status": "FORBIDDEN",
    "code": "ACCESS_DENIED",
    "message": "관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."
  }
  ```

---

## 🏁 실행 방법

```bash
# 1. 빌드
git clone https://github.com/cnxw4570123/MemberManagementSystem.git
cd MemberManagementSystem
./gradlew build

# 2. 실행
java -jar build/libs/MMS-0.0.1-SNAPSHOT.jar
```
