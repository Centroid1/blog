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
  * 로그인 상태가 아니라면 로그인 페이지로 이동한다.
* login 입력폼에는 아이디와 비밀번호를 입력하고 입력을 다 완료했으면 로그인 버튼을 통해 서버로 폼 데이터를 전송한다.
  * 아이디가 빈 문자열일 경우 '아이디를 입력해주세요' 메시지를 출력한다.
  * 비밀번호가 빈 문자열일 경우 '비밀번호를 입력해주세요' 메시지를 출력한다.**
  * 아이디와 비밀번호에 해당하는 member 데이터가 없는 경우 '아이디 또는 비밀번호를 확인해주세요'를 출력한다.**
  * 그 이외의 경우 attribute 이름이 "loginInfo"로, 값을 사용자(Member) Entity의 PK로 갖는 세션을 생성하고 
  그 세션의 만료기간을 300초로 설정하고 메인화면으로 redirect 시킨다.

<br><br>

![2-1](https://user-images.githubusercontent.com/101168818/190904433-31ae9002-4a08-4937-9b29-b57a4dc58a56.png)


<br><br><br>

![2-2](https://user-images.githubusercontent.com/101168818/190904438-71b2881f-69e3-46b9-8e1c-ef6decc935de.png)


<br><br><br>


![2-3](https://user-images.githubusercontent.com/101168818/190904443-9405cf7e-947a-4017-9cd2-87f60c4353f8.png)


<br><br><br><br>


### (03) 로그아웃 기능
* '/logout' URL로 접속하였을 때 세션을 확인하고 세션이 있으면 그 세션을 만료시키고 메인화면으로 redirect 시킨다.


<br><br><br><br>

### (04) 회원가입 기능
* '/signup' URL로 접속하였을 때 세션을 확인한다.
  * 로그인 상태라면 메인화면으로 redirect 시킨다.
  * 로그인 상태가 아니라면 회원가입 페이지로 이동한다.
* 회원가입 입력폼에는 아이디와 비밀번호, username을 입력하고 입력을 다 완료했으면 회원가입 버튼을 통해 서버로 폼 데이터를 전송한다.
  * 아이디가 빈 문자열일 경우 '아이디를 입력해주세요' 메시지를 출력한다.
  * 아이디 문자열의 길이가 10을 초과하는 경우 '아이디는 10글자 이하로 입력해주세요.' 메시지를 출력한다.
  * 이미 존재하는 아이디일 경우 '사용중인 로그인 아이디 입니다..' 메시지를 출력한다.
  * 비밀번호가 빈 문자열일 경우 '비밀번호를 입력해주세요' 메시지를 출력한다.
  * 비밀번호 문자열의 길이가 10을 초과하는 경우 '비밀번호는 10글자 이하로 입력해주세요.' 메시지를 출력한다.
  * 닉네임이 빈 문자열일 경우 '닉네임을 입력해주세요' 메시지를 출력한다.
  * 닉네임 문자열의 길이가 10을 초과하는 경우 '닉네임은 10글자 이하로 입력해주세요.' 메시지를 출력한다.
  * 그 이외의 경우 새로운 Member 데이터를 생성하고 이를 db에 저장한 후 메인화면으로 redirect 시킨다.
  
  
<br><br>


![4-1](https://user-images.githubusercontent.com/101168818/190904477-f29a29ab-5385-42ee-acb7-eb7f6d76013f.png)

<br><br><br>

![4-2](https://user-images.githubusercontent.com/101168818/190904482-4338fdb2-1d98-4b28-99f9-95adbbdd8553.png)

  
<br><br><br>

![4-3](https://user-images.githubusercontent.com/101168818/190904488-85506cd1-184a-4893-8c92-5bae6e8e8eda.png)


<br><br><br>

![4-4](https://user-images.githubusercontent.com/101168818/190904492-1eae5f02-3e60-4678-b166-3aecc738d9ce.png)


<br><br><br><br>
  
### (05) 게시글 작성 기능
* '/write' URL로 접속하였을 때 세션을 확인한다.
  * 로그인 상태라면 글 작성 페이지로 이동한다.
  * 로그인 상태가 아니라면 메인화면으로 redirect 시킨다.
* 글 작성 입력폼에는 글 제목과 내용을 입력하고 입력을 다 완료했으면 글 작성 버튼을 통해 서버로 폼 데이터를 전송한다.
  * 글 제목이 빈 문자열일 경우 '제목을 입력해주세요' 메시지를 출력한다.
  * 글 내용이 빈 문자열일 경우 '내용을 입력해주세요' 메시지를 출력한다.
  * 그 이외의 경우 새로운 Post 데이터를 생성하고 이를 db에 저장한 후 메인화면으로 redirect 시킨다.
  
<br><br>


![5-1](https://user-images.githubusercontent.com/101168818/190904656-a8f6ca42-f59b-4237-a4a9-a512026c0781.png)


<br><br><br>


![5-2](https://user-images.githubusercontent.com/101168818/190904664-fc4e2a38-1c6b-4167-8e49-99a3866db69f.png)



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


![6-1](https://user-images.githubusercontent.com/101168818/190904690-451810c3-9473-493c-8f8a-1abbf912bd25.png)

<br><br><br>

![6-2](https://user-images.githubusercontent.com/101168818/190904695-c8424ca4-a4e2-428f-ae70-04bf47341d71.png)


<br><br><br>

![6-3](https://user-images.githubusercontent.com/101168818/190904817-0a2ebcc6-f325-4f46-a20e-76765fc59b1f.png)


<br><br><br>

![6-4](https://user-images.githubusercontent.com/101168818/190911580-f2b5ab14-6aa9-4af1-a98a-854cbb7d2bda.png)



<br><br><br>

![6-5](https://user-images.githubusercontent.com/101168818/190911585-2199121b-43bc-4b3c-bdc0-89ae16e2e981.png)





<br><br><br><br>


### (07) 게시글 수정 기능
* '/post/{id}/edit' URL로 접속하였을 때 세션을 확인한다.
  * 로그인 상태라면 게시글 수정 페이지로 이동한다.
  * 로그인 상태가 아니라면 메인화면으로 redirect 시킨다.
* 게시글 수정 입력폼에는 글 제목과 내용을 입력하고 입력을 다 완료했으면 글 작성 버튼을 통해 서버로 폼 데이터를 전송한다.
  * 글 제목이 빈 문자열일 경우 '제목을 입력해주세요' 메시지를 출력한다.
  * 글 내용이 빈 문자열일 경우 '내용을 입력해주세요' 메시지를 출력한다.
  * 그 이외의 경우 새로운 Post 데이터를 수정하고 이를 db에 반영한 후 메인화면으로 redirect 시킨다.

<br><br>


![7-1](https://user-images.githubusercontent.com/101168818/190904713-59926dec-fdff-48d7-9bc9-7e45a0e8f2ab.png)


<br><br><br>

![7-2](https://user-images.githubusercontent.com/101168818/190904718-a8e51eeb-a725-45e7-85a0-ad63f5c4c3b3.png)


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


