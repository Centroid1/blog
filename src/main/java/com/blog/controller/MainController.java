package com.blog.controller;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import com.blog.domain.Comment;
import com.blog.domain.Grade;
import com.blog.domain.Member;
import com.blog.domain.Post;
import com.blog.request.*;
import com.blog.request.SignupRequest;
import com.blog.response.CommentResponse;
import com.blog.response.PostResponse;
import com.blog.service.*;


@Controller
@RequiredArgsConstructor
public class MainController {
	private final MemberService memberService;
	private final PostService postService;
	private final CommentService commentService;
	
	/*
	 * <메인화면>
	 * 최근 10개의 글 정보를 받아온다.
	 * 데이터 형태는 PostResponse 타입이다.
	 * 
	 * 세션 검사를 했을 때 현재 로그인 상태라면 mainPageLoginVersion.html을,
	 * 현재 로그인 상태가 아니라면 mainPage.html을 보며주도록 한다.
	 */
	@GetMapping("/")
	public String getMainPage(Model model, @SessionAttribute(name = "loginInfo", required = false) Long memberId) {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
		model.addAttribute("posts", postService.findAll(pageable));
		if(memberId != null) model.addAttribute("username", memberService.findById(memberId).getUsername());
		return memberId == null? "mainPage" : "mainPageLoginVersion";
	}	
	
	
	/*
	 * <로그인 기능>
	 * 로그인 페이지 GET요청
	 * 세션 검사를 했을 때 현재 로그인 상태라면 메인화면으로 redirect
	 * 그 외의 경우 정상적으로 로그인 페이지로 이동
	 */
	@GetMapping("/login")
	public String login(Model model, @SessionAttribute(name = "loginInfo", required = false) Long memberId) {
		if(memberId != null) return "redirect:/";
		model.addAttribute("loginRequest", new LoginRequest());
		return "login";
	}
		
	
	/*
	 * <로그인 기능>
	 * 로그인 데이터를 받았을 때, 다음 경우 중 하나에 해당하면 로그인 페이지로 다시 이동하여 메시지를 출력하게 한다.
	 * (01) 아이디를 빈 문자열로 전송했을 경우
	 * (02) 비밀번호를 빈 문자열로 전송했을 경우
	 * (03) 아이디와 비밀번호에 해당하는 member데이터가 없는 경우
	 * 
	 * 그 이외의 경우 세션을 5분으로 유지시키고 메인화면으로 이동
	 */
	@PostMapping("/login")
	public String login(LoginRequest loginRequest, BindingResult result, HttpServletRequest req, Model model) {		
		if(loginRequest.getLoginId().length() == 0) result.addError(new FieldError("loginRequest", "loginId", "아이디를 입력해주세요"));
		if(loginRequest.getPassword().length() == 0) result.addError(new FieldError("loginRequest", "password", "비밀번호를 입력해주세요"));
		
		Long id = memberService.login(loginRequest);
		if(id == null) result.addError(new ObjectError("loginRequest", "아이디 또는 비밀번호를 확인해주세요."));
		if(result.hasErrors()) return "login";
		
		HttpSession session = req.getSession();
		session.setMaxInactiveInterval(300);
		session.setAttribute("loginInfo", id);
		return "redirect:/";		
	}
		
	
	/*
	 * <로그아웃 기능>
	 * 세션이 있으면 그 세션을 만료시키고 메인화면으로 이동
	 */
	@GetMapping("/logout")
	public String logout(HttpServletRequest req, @SessionAttribute(name = "loginInfo", required = false) Long memberId) {
		if(memberId != null) req.getSession(false).invalidate();
		return "redirect:/";
	}
		
	
	/*
	 * 회원가입 기능
	 * 세션 검사를 했을 때 현재 로그인 상태라면 메인화면으로 이동
	 * 현재 로그인 상태가 아니라면 회원가입 페이지로 이동
	 */
	@GetMapping("/signup")
	public String signup(Model model, @SessionAttribute(name = "loginInfo", required = false) Long memberId) {
		if(memberId != null) return "redirect:/";
		model.addAttribute("signupRequest", new SignupRequest());
		return "signup";
	}	
	
	
	/*
	 * 회원가입 데이터를 받았을 때, 다음 경우중 하나에 해당하면 로그인 페이지로 다시 이동하여 메시지를 출력하게 한다.
	 * (01) 아이디를 빈 문자열로 전송했을 경우
	 * (02) 비밀번호를 빈 문자열로 전송했을 경우
	 * (03) 닉네임을 빈 문자열로 전송했을 경우
	 * (04) 중복 아이디가 있는 경우
	 * 그 이외의 경우 새로운 Member데이터를 만들고 db에 저장
	 */
	@PostMapping("/signup")
	public String signup(SignupRequest signupRequest, BindingResult result) {
		if(signupRequest.getLoginId().length() == 0) result.addError(new FieldError("signupRequest", "loginId", "아이디를 입력해주세요"));
		if(signupRequest.getPassword().length() == 0) result.addError(new FieldError("signupRequest", "password", "비밀번호를 입력해주세요"));
		if(signupRequest.getUsername().length() == 0) result.addError(new FieldError("signupRequest", "username", "닉네임을 입력해주세요"));
		if(result.hasErrors()) return "signup";		
		if(memberService.findByLoginId(signupRequest.getLoginId()) != null) 
			result.addError(new ObjectError("signupRequest", "사용중인 로그인 아이디 입니다."));
		if(result.hasErrors()) return "signup";
		
		
		Member member = Member.builder()
				.loginId(signupRequest.getLoginId())
				.password(signupRequest.getPassword())
				.username(signupRequest.getUsername())
				.build();
		memberService.save(member);				
				
		
		return "redirect:/";
	}
	
	
	
	
	@GetMapping("/write")
	public String writePost(@SessionAttribute(name = "loginInfo", required = false) Long memberId) {
		if(memberId == null) return "redirect:/login";	
		return "postWrite";
	}
	
	/*
	 * <글 작성 기능> 
	 * 세션 검사를 했을 때 세션이 만료가 되었다면 메인화면으로 redirect 시킨다.
	 * 그 이외의 경우 Post 데이터를 만들고 db에 저장한 뒤 메인화면으로 redirect 
	 */
	@PostMapping("/write")
	public String writePost(@SessionAttribute(name = "loginInfo", required = false) Long memberId, PostCreate postCreate) {
		if(memberId == null) return "redirect:/";		
		Member member = memberService.findById(memberId);
		
		
		Post post = Post.builder()
				.title(postCreate.getTitle())
				.content(postCreate.getContent())
				.member(member)
				.build();
		
		
		postService.save(post);
		member.getPosts().add(post);				
		return "redirect:/";
	}
	
	
	/*
	 * <글 조회 기능>
	 * postResponse DTO로 화면에 출력할 데이터를 받아온다.
	 * postId로 식별되는 post가 없다면 postNotFound 화면을 보여준다.
	 * 세션 검사를 했을 때 로그인한 사용자가 없다면 postRead.html을,
	 * 현재 로그인 중이라면 postReadLoginVersion.html 화면을 보여주도록 한다.
	 */
	@GetMapping("/posts/{id}")
	public String getPost(@PathVariable(name = "id") Long postId, Model model, @SessionAttribute(name = "loginInfo", required = false) Long memberId) {		
		Member member = null;
		if(memberId != null) member = memberService.findById(memberId);				
		if(member != null) model.addAttribute("username", member.getUsername());
		
		Post post = postService.findById(postId);
		if(post == null) return memberId == null? "postNotFound" : "postNotFoundLoginVersion";
		
		
		
		PostResponse postResponse = PostResponse.builder()
												.postId(post.getId())
												.loginMemberId(memberId)
												.title(post.getTitle())
												.content(post.getContent())
												.postAuthority(post.getMember().getId() == memberId || (member != null && member.getGrade() == Grade.MANAGER))
												.build();
		for(Comment comment : post.getComments()) {
			postResponse.getComments().add(CommentResponse.builder()
														  .commentId(comment.getId())
														  .username(comment.getMember().getUsername())
														  .content(comment.getContent())
														  .commentAuthority(comment.getMember().getId() == memberId || (member != null && member.getGrade() == Grade.MANAGER))
														  .build());			
		}
		
		
		model.addAttribute("post", postResponse);	
		return memberId == null? "postRead" : "postReadLoginVersion";
	}
	
	
	
	@PostMapping("/posts/{id}/comment")
	public String createComment(@PathVariable Long id, @SessionAttribute(name = "loginInfo", required = false) Long memberId, CommentCreate commentCreate) {	
		if(memberId == null) return "redirect:/posts/" + id;
		if(memberId != commentCreate.getLoginMemberId()) return "redirect:/posts/" + id;		
		commentService.save(id, commentCreate);
		return "redirect:/posts/" + id;
	}
	
	
	
	/*
	 * <게시글 수정 기능>
	 * 세션 검사를 했을 때 세션이 만료가 되었다면 메인화면으로 redirect 시킨다.
	 * postId로 식별되는 게시글을 찾는데 이 게시글이 없다면 postNotFound 화면을 보여주도록 redirect 시킨다.
	 * 그 이외의 경우 postEdit(게시글 수정화면) 페이지로 이동한 후
	 * 그 페이지로 부터 폼 입력을 받아서 데이터를 수정한 것을 db에 저장하고 원래화면으로 redirect
	 */
	@GetMapping("/posts/{id}/edit")
	public String editPost(@PathVariable(name = "id") Long postId, @SessionAttribute(name = "loginInfo", required = false) Long memberId, Model model) {
		if(memberId == null) return "redirect:/"; // 세션을 검사했더니 로그인 상태가 아니면 메인화면으로 돌아가기
		Member member = memberService.findById(memberId);
			
		Post post = postService.findById(postId);
		model.addAttribute("username", member.getUsername());
		if(post == null) return memberId == null? "postNotFound" : "postNotFoundLoginVersion";
									
		model.addAttribute("post", post);
		return "postEdit";
	}
	
	@PostMapping("/posts/{id}/edit")
	public String editPost(@PathVariable(name = "id") Long postId, @SessionAttribute(name = "loginInfo", required = false) Long memberId, PostEdit postEdit) {		
		if(memberId == null) return "redirect:/"; // 세션을 검사했더니 로그인 상태가 아니면 메인화면으로 돌아가기		
		postService.edit(postId, postEdit);
		return "redirect:/posts/" + postId;
	}
	
	
	
	/*
	 * <게시글 삭제 기능>
	 * 세션 검사를 했을 때 세션이 만료가 되었다면 메인화면으로 redirect 시킨다.
	 * 그 이외의 경우 게시글을 삭제하고 메인화면으로 redirect
	 */
	@PostMapping("/posts/{id}/delete")
	public String deletePost(@PathVariable Long id, @SessionAttribute(name = "loginInfo", required = false) Long memberId) {
		if(memberId == null) return "redirect:/"; // 세션을 검사했더니 로그인 상태가 아니면 메인화면으로 돌아가기	
		postService.deleteById(id);
		return "redirect:/";
	}
	
	
	
	/*
	 * <댓글 삭제 기능>
	 * 세션 검사를 했을 때 세션이 만료가 되었다면 메인화면으로 redirect 시킨다.
	 * 그 이외의 경우 댓글을 삭제하고 원래 페이지로 돌아간다.
	 */
	@PostMapping("/comments/{commentId}/delete")
	public String deleteComment(@PathVariable Long commentId, @SessionAttribute(name = "loginInfo", required = false) Long memberId, Long postId) {
		if(memberId == null) return "redirect:/"; // 세션을 검사했더니 로그인 상태가 아니면 메인화면으로 돌아가기	
		commentService.deleteById(commentId);
		return "redirect:/posts/" + postId;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@PostConstruct
	public void init() {
		Member manager = Member.builder().loginId("manager").password("manager").username("manager").build();
		manager.setGrade(Grade.MANAGER);
		memberService.save(manager);
		
		Member member1 = Member.builder().loginId("v1").password("v1").username("v1").build();
		Member member2 = Member.builder().loginId("v2").password("v2").username("v2").build();
		memberService.save(member1);
		memberService.save(member2);
		
		Post post0 = Post.builder()
				.title("운영자 정보")
				.content("아이디 : manager\n비밀번호: manager\n닉네임: manager\n운영자 정보로 로그인 하면 모든 글을 수정/삭제 할 수 있습니다."
						+ "\n또한 모든 댓글을 삭제할 수 있습니다.")
				.member(manager)
				.build();
		
		
		Post post1 = Post.builder()
				.title("사용자 v1 정보")
				.content("아이디 : v1\n비밀번호: v1\n닉네임: v1\n")
				.member(member1)
				.build();
		
		Post post2 = Post.builder()
				.title("사용자 v2 정보")
				.content("아이디 : v2\n비밀번호: v2\n닉네임: v2\n")
				.member(member2)
				.build();
		
		postService.save(post0);
		postService.save(post1);
		postService.save(post2);
		
		
		CommentCreate commentCreate1 = CommentCreate.builder().loginMemberId(member1.getId()).content("v1이 작성한 댓글").build();
		CommentCreate commentCreate2 = CommentCreate.builder().loginMemberId(member2.getId()).content("v2가 작성한 댓글").build();
		CommentCreate commentCreate3 = CommentCreate.builder().loginMemberId(member1.getId()).content("v1이 작성한 댓글").build();
		CommentCreate commentCreate4 = CommentCreate.builder().loginMemberId(member2.getId()).content("v2가 작성한 댓글").build();
		
		commentService.save(post1.getId(), commentCreate1);
		commentService.save(post1.getId(), commentCreate2);
		commentService.save(post2.getId(), commentCreate3);
		commentService.save(post2.getId(), commentCreate4);
	}		
}