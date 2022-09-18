# 스프링부트와 JPA를 이용하여 만들어보는 게시판

<br>

## 1. 구현사항 명세서

### (01) 메인화면
* '/' URL로 접속하였을 때 세션을 확인한다.
  * 로그인 상태라면 우측상단에 회원의 username과 로그아웃 글씨가 보이게 한다.
  * 미로그인 상태라면 우측상단에 회원가입과 로그인 글씨가 보이게 한다.
* 1페이지당 10개의 게시글 정보를 가져온다.
  * 표에는 각각의 게시물의 게시물번호, 제목, 작성자, 작성시간을 나타낸다.
* 좌측하단에는 글작성 버튼이 있다.
  * 로그인 상태라면 글 작성 페이지로 이동한다.
  * 미로그인 상태라면 로그인화면으로 이동한다.
<br><br>


![1-1](https://user-images.githubusercontent.com/101168818/190903641-b457581e-8fa4-4583-a1dc-145263e577b4.png)



<br><br><br>

![1-2](https://user-images.githubusercontent.com/101168818/190903646-1cb88b4d-e629-4c06-85ca-7e8a803d3e50.png)


<br><br><br>

![1-3](https://user-images.githubusercontent.com/101168818/190903651-2aaf0aea-31ea-4207-b3c4-04a95836ad62.png)


<br><br><br><br>





### (02) 로그인 기능
* '/login' URL로 접속하였을 때 세션을 확인한다.
  * 로그인 상태라면 메인화면으로 redirect 시킨다.
  * 미로그인 상태라면 우측상단에 회원가입과 로그인 글씨가 보이게 하고 loginForm을 보여준다.
* login 입력폼에는 아이디와 비밀번호를 입력하고 입력을 다 완료했으면 로그인 버튼을 통해 서버로 폼 데이터를 전송한다.
  * **아이디 문자열이 빈 문자열일 경우 '아이디를 입력해주세요' 메시지를 출력한다.**
  * **비밀번호 문자열이 빈 문자열일 경우 '비밀번호를 입력해주세요' 메시지를 출력한다.**
  * **입력받은 아이디 문자열을 아이디로 갖는 사용자가 없거나 그 사용자의 비밀번호가 입력된 비밀번호와 다를 경우 '아이디 또는 비밀번호를 확인해주세요'를 출력한다.**
  * **(위의 3가지 사항 모두 미구현 사항은 아닌데 조금씩 이상하게 동작해서 수정이 필요함)**
  * 그 이외의 경우 attribute 이름이 "loginInfo"로, 값을 사용자(Member) Entity의 PK로 갖는 세션을 생성하고 
  그 세션의 만료기간을 300초로 설정하고 메인화면으로 redirect 시킨다.

<br><br>

![2-1](https://user-images.githubusercontent.com/101168818/184197366-98f209eb-1ce6-433e-879e-6dbac3d6f85c.JPG)


<br><br><br>

![2-2](https://user-images.githubusercontent.com/101168818/184197373-4413bbcd-2697-4372-9062-3349ce03f230.JPG)

<br><br><br><br>


### (03) 로그아웃 기능
* '/logout' URL로 접속하였을 때 세션을 확인하고 세션이 있으면 그 세션을 만료시키고 메인화면으로 redirect 시킨다.


<br><br><br><br>

### (04) 회원가입 기능
* '/signup' URL로 접속하였을 때 세션을 확인한다.
  * 로그인 상태라면 메인화면으로 redirect 시킨다.
  * 미로그인 상태라면 우측상단에 회원가입과 로그인 글씨가 보이게 하고 signupForm을 보여준다.
* signupForm 입력폼에는 아이디와 비밀번호, username을 입력하고 입력을 다 완료했으면 회원가입 버튼을 통해 서버로 폼 데이터를 전송한다.
  * **아이디 문자열이 빈 문자열일 경우 '아이디를 입력해주세요' 메시지를 출력한다.**
  * **비밀번호 문자열이 빈 문자열일 경우 '비밀번호를 입력해주세요' 메시지를 출력한다.**
  * **username 문자열이 빈 문자열일 경우 '닉네임을 입력해주세요' 메시지를 출력한다.**
  * **입력받은 아이디 문자열을 아이디로 갖는 사용자가 있다면 '사용중인 로그인 아이디 입니다'를 출력한다.**
  * **(위의 4가지 사항 모두 미구현 사항은 아닌데 조금씩 이상하게 동작해서 수정이 필요함)**
  * 그 이외의 경우 입력받은 폼 데이터를 이용하여 Member Entity를 생성하고 데이터베이스에 저장한다.
  
  
<br><br>

![4-1](https://user-images.githubusercontent.com/101168818/184197789-14fb0fbf-4eb9-4f84-8859-c31a2295d9bc.JPG)

<br><br><br>
  ![4-2](https://user-images.githubusercontent.com/101168818/184197813-2672a55c-716a-4bad-816e-e2b9ac1ed64c.JPG)

  
<br><br><br>
  ![4-3](https://user-images.githubusercontent.com/101168818/184197821-060c9e80-f6e3-4e68-9cae-538aa5ea084a.JPG)


<br><br><br><br>
  
### (05) 게시글 작성 기능
* '/wrtie' URL로 접속하였을 때 세션을 확인한다.
  * 로그인 상태라면 게시글 입력 폼 화면이 보이게 하고 우측상단에 회원의 username과 logout 글싸가 보이게 한다.
  * 미로그인 상태라면 메인화면으로 redirect 시킨다.
* 입력폼에는 게시글 제목과 게시글 내용을 입력할 수 있고 글 작성 버튼을 통해 서버로 폼 데이터를 전송한다.
  * **게시글 제목이 빈 문자열일 경우 '제목을 입력해주세요' 메시지를 출력한다. (미구현사항)**
  
<br><br>

![5-1](https://user-images.githubusercontent.com/101168818/184198146-a777ec67-a3c9-4a43-a174-f3cf64bd29bd.JPG)


<br><br><br><br>
  
  
  
  
  
### (06) 게시글 조회 기능
#### (06) - 1 로그인 상태일 때
* '/posts/{postId}' URL로 접속하였을 때 postId를 PK로 갖는 post 데이터를 가져온다.
  * DB에 데이터가 없으면 우측상단에 회원의 username과 logout 글씨가 보이게 하고 화면 중앙에 '글이 존재하지 않습니다'를 출력한다.
* 우측상단에 회원의 username과 logout 글씨가 보이게 한다.
* 가져온 post 데이터의 제목과 내용을 화면에 보여준다.
* 게시글 수정과 삭제 권한이 있으면 '수정하기', '삭제하기' 두 버튼이 화면에 보이게 하고 권한이 없으면 버튼을 화면에 출력하지 않는다.
* '댓글을 작성해주세요' 라는 글씨를 출력한다.
* post데이터의 댓글 List를 출력한다. 각 댓글에 대하여 username과 댓글의 내용이 화면에 출력된다.
  * 각 댓글에 대하여 삭제 권한이 있으면 '삭제하기' 버튼이 화면에 보이게 하고 권한이 없으면 버튼을 화면에 출력하지 않는다.

#### (06) - 2 미로그인 상태일 때
* '/posts/{postId}' URL로 접속하였을 때 postId를 PK로 갖는 post 데이터를 가져온다.
  * DB에 데이터가 없으면 우측상단에 회원가입과 로그인 글씨가 보이게 하고 화면 중앙에 '글이 존재하지 않습니다'를 출력한다.
* 우측상단에 회원가입과 로그인 글씨가 보이게 한다.
* 가져온 post 데이터의 제목과 내용을 화면에 보여준다.
* '댓글을 작성 하시려면 로그인 해주세요' 라는 글씨를 출력한다.
* post데이터의 댓글 List를 출력한다. 각 댓글에 대하여 username과 댓글의 내용이 화면에 출력된다.


<br><br>
![6-1](https://user-images.githubusercontent.com/101168818/184279086-6b084e20-0b76-43c1-b18a-719b7e212219.png)


<br><br><br>

![6-2](https://user-images.githubusercontent.com/101168818/184279089-d77edcd3-d43b-4aa5-8808-c5e5f5ebe15f.png)


<br><br><br><br>


### (07) 게시글 수정 기능
* '/post/{id}/edit' URL로 접속하였을 때 세션을 확인한다.
  * 미로그인 상태라면 메인화면으로 redirect 시킨다.
* 우측상단에 회원의 username과 logout 글씨가 보이게 한다.
* 좌측하단에는 수정하기 버튼이 있다.
* 수정할 제목과 수정할 내용을 폼에 채워넣고 수정하기 버튼을 전송하면 글이 수정된다.
  * **전송한 데이터의 제목이 빈 문자열이면 '제목을 입력해주세요' 메시지를 출력한다. (미구현사항)**

<br><br>



![7-1](https://user-images.githubusercontent.com/101168818/184198432-c94fb186-ae5b-4eb8-b203-251a0200074e.JPG)





<hr>
<br><br><br>

## 2. Controller에서의 HTTP 요청
URL | HTTP 메소드 | 역할
---|:---:|---:
`"/"` | `GET` | 메인화면 접속
`"/login"` | `GET` | 로그인 화면 접속
"/logout"` | `GET` | 로그아웃
"/signup"` | `GET` | 회원가입 화면 접속
"/signup"` | `POST` | 회원가입 폼 데이터 전송
"/write"` | `GET` | 글 작성 페이지 접속
"/write"` | `POST` | 글 작성 폼 데이터 전송
"/posts/{id}"` | `GET` | id로 식별되는 post 열람
"/posts/{id}/comment"` | `POST` | id로 식별되는 post에 댓글 작성
"/posts/{id}/edit"` | `GET` | id로 식별되는 post를 수정하는 화면 접속
"/posts/{id}/edit"` | `POST` | id로 식별되는 post를 수정하는 폼 데이터 전송
"/posts/{id}/delete"` | `POST` | id로 식별되는 post 
"/comments/{id}/delete"` | `POST` | id로 식별되는 comment 삭제




<br>



![button](https://user-images.githubusercontent.com/101168818/184031296-3ab09363-9b77-47e5-a15d-51e891dbfa16.JPG)
