# 나만의 웹서버 만들기

## 📌 설명
이 프로젝트는 Java의 Socket API만을 사용하여, SpringBoot나 Tomcat 같은 
프레임워크/컨테이너 없이 HTTP 웹 서버를 직접 구현하는 도전 과제입니다.

우아한테크코스 프리코스 마지막 오픈 미션으로 진행되었으며, '결과보다는 과정', 
'실패를 두려워하지 않는 몰입'을 목표로 개발하였습니다.

프로젝트를 진행하며 TDD(테스트 주도 개발)를 통해 단단한 설계를 하고, 
스레드 풀(Thread Pool)을 도입하여 동시성 이슈를 해결하는 등 안정적인 서버를 만들려고 노력했습니다.

## 💡 계기
> "SpringBoot 내부에서 동작하는 웹 서버는 도대체 어떤 방식으로 요청을 받아서 처리할까?"

평소 SpringBoot를 통해 개발을 하였습니다. 그러면서 SpringBoot가 제공하는 웹 서버에 대해 궁금해졌습니다.
네트워크와 HTTP의 본질을 이해하고자 시작하게 되었습니다.

## 🎯 목표
- [x] TCP 소켓 서버 구현 : `ServerSocket`을 이용한 포트 바인딩 및 연결
- [x] HTTP 요청 라인/헤더 파싱 : RequestLine(Method, Path), Headers 파싱 로직 구현
- [x] 라우팅 및 기본 핸들러 : 요청 URL에 따른 동적 분기 처리
- [x] 정적 파일 서빙 : `/index.html`같은 리소스 파일 읽기 및 응답
- [x] 에러 처리 : 잘못된 요청 및 존재하지 않는 파일에 대한 `404 Not Found` 처리 
- [x] 멀티스레드 처리 : `ExecutorService`를 활용한 스레드 풀 적용

## 🔄 흐름
MVC 패턴과 유사한 구조로 역할을 분리하여 설계했습니다.
```
Client  ->  WebServer (Port 8080)
                ↓
           (Thread Pool)
                ↓
           RequestHandler  (Controller)
             ↙        ↘
     HttpRequest    HttpResponse
```

1. Client가 요청을 보냅니다.
2. WebServer가 연결을 수락하고 Thread Pool에서 스레드를 하나 할당합니다.
3. RequestHandler가 실행되며 HttpRequest를 통해 데이터를 파싱합니다.
4. 파싱된 데이터를 기반으로 라우팅 로직을 수행합니다.
5. HttpResponse를 통해 규격에 맞는 HTTP 응답 메시지를 생성하여 전송합니다.


## 🗂️ 파일 구조
```
src\main\java\mywebserver
│  Application.java
│  WebServer.java
│
├─controller
│      RequestHandler.java
│
├─http
│      HttpHeaders.java
│      HttpMethod.java
│      HttpRequest.java
│      HttpResponse.java
│      HttpStartLine.java
│
├─util
│      ErrorMessage.java
│
└─view
       OutputView.java
```

## 🏃 실행 방법
1. Application.main()을 실행합니다.
2. http://localhost:8080에 접속합니다.
3. `Hello World` 문구가 잘 나오는지 확인합니다.
4. http://localhost:8080/index.html 에 접속합니다.
5. `Hello File` 문구가 잘 나오는지 확인합니다.
6. http://localhost:8080/abc (abc말고 다른 것도 가능)에 접속합니다.
7. `404 Not Found` 문구가 잘 나오는지 확인합니다.

## 🧪 테스트 및 검증
이 프로젝트는 기능 검증을 위한 단위 테스트와 서버 안정성 검증을 위한 부하 테스트를 제공합니다.

### 1. 단위 테스트 (Unit Test)

HttpHeaders, HttpRequest, HttpResponse, RequestHandler의 로직을 TDD로 검증.

#### 터미널에서 실행
```
./gradlew clean test
```

* 빌드 결과는 build/reports/tests/test/index.html에서 확인할 수 있습니다.

### 2. 부하 테스트 (Load Test)

직접 구현한 ConcurrentLoadTest 도구를 사용하여, 다수의 사용자가 동시에 접속할 때 서버가 안정적으로 동작하는지 검증합니다.

#### 실행 절차
1. 서버 실행: 먼저 Application.main()을 실행하여 웹 서버를 켭니다.
2. 테스트 도구 실행: 서버가 켜진 상태에서 src/test/java/webserver/ConcurrentLoadTest.java의 main() 메서드를 실행합니다.

#### 검증 시나리오
- 동시 접속: 20명의 사용자가 동시에 요청을 시도합니다.
- 총 요청: 총 100개의 HTTP GET 요청을 보냅니다.
- 검증 항목:
  - 모든 요청에 대해 200 OK 응답을 받았는가?
  - 스레드 풀이 설정된 최대 개수(50개)를 넘지 않고 재사용되었는가?

#### 실행 결과 예시
```
========================================
부하 테스트 결과 리포트
========================================
총 소요 시간    : 352 ms
성공 요청       : 100
실패 요청       : 0
========================================
[PASS] 서버가 모든 요청을 안정적으로 처리했습니다!
```