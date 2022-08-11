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

### (02) 로그인 기능
* '/login' URL로 접속하였을 때 세션을 확인한다.
  * 로그인 상태라면 메인화면으로 redirect 시킨다.
  * 미로그인 상태라면 우측상단에 회원가입과 로그인 글씨가 보이게 하고 loginForm을 보여준다.
* login 입력폼에는 아이디와 비밀번호를 입력하고 입력을 다 완료했으면 로그인 버튼을 통해 서버로 폼 데이터를 전송한다.
  * 아이디 문자열이 빈 문자열일 경우 '아이디를 입력해주세요' 메시지를 출력한다.
  * 비밀번호 문자열이 빈 문자열일 경우 '비밀번호를 입력해주세요' 메시지를 출력한다.
  * 입력받은 아이디 문자열을 아이디로 갖는 사용자가 없거나 그 사용자의 비밀번호가 입력된 비밀번호와 다를 경우 '아이디 또는 비밀번호를 확인해주세요'를 출력한다.
  * 그 이외의 경우 attribute 이름이 "loginInfo"로, 값을 사용자(Member) Entity의 PK로 갖는 세션을 생성하고 
  그 세션의 만료기간을 300초로 설정하고 메인화면으로 redirect 시킨다.

### (03) 로그아웃 기능
* '/logout' URL로 접속하였을 때 세션을 확인하고 세션이 있으면 그 세션을 만료시키고 메인화면으로 redirect 시킨다.

### (04) 회원가입 기능
* '/signup' URL로 접속하였을 때 세션을 확인한다.
  * 로그인 상태라면 메인화면으로 redirect 시킨다.
  * 미로그인 상태라면 우측상단에 회원가입과 로그인 글씨가 보이게 하고 signupForm을 보여준다.
* signupForm 입력폼에는 아이디와 비밀번호, username을 입력하고 입력을 다 완료했으면 회원가입 버튼을 통해 서버로 폼 데이터를 전송한다.
  * 아이디 문자열이 빈 문자열일 경우 '아이디를 입력해주세요' 메시지를 출력한다.
  * 비밀번호 문자열이 빈 문자열일 경우 '비밀번호를 입력해주세요' 메시지를 출력한다.
  * username 문자열이 빈 문자열일 경우 '닉네임을 입력해주세요' 메시지를 출력한다.
  * 입력받은 아이디 문자열을 아이디로 갖는 사용자가 있다면 '사용중인 로그인 아이디 입니다'를 출력한다.
  * 그 이외의 경우 입력받은 폼 데이터를 이용하여 Member Entity를 생성하고 데이터베이스에 저장한다.
  
### (05) 게시글 작성 기능
* '/wrtie' URL로 접속하였을 때 세션을 확인한다.
  * 로그인 상태라면 게시글 입력 폼 화면이 보이게 하고 우측상단에 회원의 username과 logout 글싸가 보이게 한다.
  * 미로그인 상태라면 메인화면으로 redirect 시킨다.
* 입력폼에는 게시글 제목과 게시글 내용을 입력할 수 있고 글 작성 버튼을 통해 서버로 폼 데이터를 전송한다.
  * 아이디 문자열이 빈 문자열일 경우 '아이디를 입력해주세요' 메시지를 출력한다.
  * 비밀번호 문자열이 빈 문자열일 경우 '비밀번호를 입력해주세요' 메시지를 출력한다.
  * username 문자열이 빈 문자열일 경우 '닉네임을 입력해주세요' 메시지를 출력한다.
  * 입력받은 아이디 문자열을 아이디로 갖는 사용자가 있다면 '사용중인 로그인 아이디 입니다'를 출력한다.
  * 그 이외의 경우 입력받은 폼 데이터를 이용하여 Member Entity를 생성하고 데이터베이스에 저장한다.
  
### (06) 게시글 조회 기능
* '/posts/{postId}' URL로 접속하였을 때 postId를 PK로 갖는 post가 있는지 DB에서 확인한다
  * DB에 데이터가 없으면
    * 세션을 확인하였을 때 로그인 상태라면 우측상단에 회원의 username과 logout 글싸가 보이게 하고 화면 중앙에 '글이 존재하지 않습니다'를 출력한다.
    * 세션을 확인하였을 때 미로그인 상태라면 우측상단에 회원가입과 로그인 글씨가 보이게 하고 화면 중앙에 '글이 존재하지 않습니다'를 출력한다.
  * DB에 데이터가 있으면
    * 세션을 확인하였을 때 로그인 상태라면 우측상단에 회원의 username과 logout 글싸가 보이게 한다.
    * 세션을 확인하였을 때 미로그인 상태라면 우측상단에 회원가입과 로그인 글씨가 보이게 한다.
  * postId로 식별되는 post데이터를 받아와서 화면에 출력한다. 화면에는 게시글의 제목과  
* 입력폼에는 게시글 제목과 게시글 내용을 입력할 수 있고 글 작성 버튼을 통해 서버로 폼 데이터를 전송한다.
  * 아이디 문자열이 빈 문자열일 경우 '아이디를 입력해주세요' 메시지를 출력한다.
  * 비밀번호 문자열이 빈 문자열일 경우 '비밀번호를 입력해주세요' 메시지를 출력한다.
  * username 문자열이 빈 문자열일 경우 '닉네임을 입력해주세요' 메시지를 출력한다.
  * 입력받은 아이디 문자열을 아이디로 갖는 사용자가 있다면 '사용중인 로그인 아이디 입니다'를 출력한다.
  * 그 이외의 경우 입력받은 폼 데이터를 이용하여 Member Entity를 생성하고 데이터베이스에 저장한다.

### (07) 게시글 수정 기능
#### (07) - 1 로그인 상태일 때

#### (07) - 2 미로그인 상태일 때

### (08) 게시글 삭제 기능


### (09) 댓글 작성 기능


### (10) 댓글 삭제 기능

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


![button](https://user-images.githubusercontent.com/101168818/184031296-3ab09363-9b77-47e5-a15d-51e891dbfa16.JPG)
