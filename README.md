# 스프링부트와 JPA를 이용하여 만들어보는 게시판

<br>

## 1. 구현사항 명세서

### (01) 메인화면
* '/' URL로 접속하였을 때 세션을 확인한다.
  * 로그인 상태라면 우측상단에 회원의 username과 logout 글싸가 보이게 한다.
  * 미로그인 상태라면 우측상단에 회원가입과 로그인 글씨가 보이게 한다.
* 가장 최근에 작성된 게시글이 맨 위로 오도록 게시글 목록 표를 보여준다.
  * 표에는 각각의 게시물의 게시물번호, 제목, 작성자, 작성시간을 나타낸다.
* 좌측하단에는 글작성 버튼이 있다.
  * 로그인 상태라면 글 작성 페이지로 이동한다.
  * 미로그인 상태라면 로그인화면으로 이동한다.

### (02) 게시글 수정

<br>

## 2. Controller에서의 HTTP 요청
URL | HTTP 메소드 | 역할
---|:---:|---:
`"/"` | `GET` | 메인화면
`"/login"` | `GET` | 메인화면
`absolute` | `GET` | 메인화면
`fixed` | `GET` | 메인화면




<br>

## 3. 데이터베이스 ERD




<br>


## 3. 아직 미구현 사항 


![button](https://user-images.githubusercontent.com/101168818/184031296-3ab09363-9b77-47e5-a15d-51e891dbfa16.JPG)
