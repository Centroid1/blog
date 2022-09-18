package com.blog.controller;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import lombok.*;
import com.blog.domain.*;
import com.blog.request.*;
import com.blog.response.*;
import com.blog.service.*;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class MainController {
	private final MemberService memberService;
	private final PostService postService;
	private final CommentService commentService;
	
		
	/*
	 * <메인화면>
	 * page라는 query parameter의 값을 읽어서 몇페이지의 정보를 읽어올 것인지를 결정한다.
	 * query parameter의 값이 없을 경우 default로 1페이지의 정보를 반환한다. (첫페이지가 1페이지이다.)
	 * 그리고 query parameter의 값이 다음 중 하나의 경우라면 1페이지의 정보를 반환하도록(default와 같도록) 한다.
	 *   (01) 문자열의 길이가 7이상인 경우
	 *   (02) query parameter의 문자열에 숫자가 포함되지 않은 경우('-'기호도 포함, 즉, 음수를 허용하지 않는다.)
	 *   (03) 현재 저장된 게시글을 담을 수 있는 페이지 수보다 더 큰 숫자의 페이지를 요청할 경우
	 *   (04) 0번 페이지를 요청할 경우 
	 * 
	 * 예를 들어, 게시글이 35개가 있으면
	 * 1페이지: 가장 최근에 작성된 게시글 10개
	 * 2페이지: 가장 최근에 작성된 순서로 11번째 글 ~ 20번째 글
	 * 3페이지: 가장 최근에 작성된 순서로 21번째 글 ~ 30번째 글
	 * 4페이지: 가장 최근에 작성된 순서로 31번째 글 ~ 35번째 글
	 * 5페이지 이상: 존재하지 않음
	 * 이런 식으로 페이지가 구성되는데 이 상태에서 6페이지를 요청하면 1페이지 글을 보여준다.
	 *  
	 * 
	 * 
	 * 세션 검사를 했을 때 현재 로그인 상태라면 mainPageLoginVersion.html을,
	 * 세션 검사를 했을 때 현재 로그인 상태가 아니라면 mainPage.html을 보며주도록 한다.
	 */
	@GetMapping("/")
	public String getMainPage(Model model, @SessionAttribute(name = "loginInfo", required = false) Long memberId, 
			@RequestParam(name = "page", defaultValue = "1") String page_input) {
		if(page_input.length() > 6) return "redirect:/";
		for(int i = 0; i < page_input.length(); i++)
		if('0' > page_input.charAt(i) || page_input.charAt(i) > '9')
			return "redirect:/";
		
		
		int page = Math.max(Integer.parseInt(page_input), 1);				
		Long numOfPost = postService.getNumOfPost();
		Long numOfPage = (numOfPost % 10 == 0? numOfPost / 10 : numOfPost / 10 + 1);
		if(numOfPage < page) return "redirect:/";
		
		
		model.addAttribute("posts", postService.findAll(page - 1));		
		
		
		List<Integer> pageBar = new LinkedList<>();
		if(page >= 11) {
			pageBar.add(-1);
			model.addAttribute("prev_page", page - 1);
		}
		for(int i = ((page - 1) / 10) * 10 + 1; i <= Math.min(((page - 1) / 10) * 10 + 10, numOfPage); i++) 
			pageBar.add(i);
		if(numOfPage > ((page - 1) / 10) * 10 + 10) {
			pageBar.add(-2);
			model.addAttribute("next_page", page + 1);			
		}
		model.addAttribute("cur_page", page);
		model.addAttribute("pageBar", pageBar);
		
		
		if(memberId != null) model.addAttribute("username", memberService.findById(memberId).getUsername());
		return memberId == null? "mainPage" : "mainPageLoginVersion";
	}	
	
	
	
	
	
	
	/*
	 * <로그인 기능>
	 * 세션 검사를 했을 때 현재 로그인 상태라면 메인화면으로 redirect
	 * 세션 검사를 했을 때 현재 로그인 상태가 아니라면 로그인 페이지로 이동
	 */
	@GetMapping("/login")
	public String login(Model model, @SessionAttribute(name = "loginInfo", required = false) Long memberId) {
		if(memberId != null) return "redirect:/";
		model.addAttribute("loginRequest", new LoginRequest());
		return "login";
	}
		
	
	
	
	
	
	
	/*
	 * <로그인 기능>
	 * 로그인 데이터를 받았을 때, 다음 경우 중 하나에 해당하면 로그인 페이지로 다시 이동하여 에러 메시지를 출력하게 한다.
	 * (01) 아이디가 빈 문자열일 경우
	 * (02) 비밀번호가 빈 문자열일 경우
	 * (03) 아이디와 비밀번호에 해당하는 member데이터가 없는 경우
	 * 
	 * 위 3가지 경우에 해당하지 않으면 세션을 5분으로 유지시키고 메인화면으로 redirect
	 */
	@PostMapping("/login")
	public String login(LoginRequest loginRequest, BindingResult result, HttpServletRequest req, Model model) {		
		if(loginRequest.getLoginId().length() == 0) result.addError(new FieldError("loginRequest", "loginId", "아이디를 입력해주세요"));
		if(loginRequest.getPassword().length() == 0) result.addError(new FieldError("loginRequest", "password", "비밀번호를 입력해주세요"));
		if(result.hasErrors()) return "login";
				
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
	 * 세션 검사를 했을 때 현재 로그인 상태라면 그 세션을 만료시키고 메인화면으로 redirect
	 */
	@GetMapping("/logout")
	public String logout(HttpServletRequest req, @SessionAttribute(name = "loginInfo", required = false) Long memberId) {
		if(memberId != null) req.getSession(false).invalidate();
		return "redirect:/";
	}
		
	
	
	
	/*
	 * <회원가입 기능>
	 * 세션 검사를 했을 때 현재 로그인 상태라면 메인화면으로 redirect
	 * 세션 검사를 했을 때 현재 로그인 상태가 아니라면 회원가입 페이지로 이동
	 */
	@GetMapping("/signup")
	public String signup(Model model, @SessionAttribute(name = "loginInfo", required = false) Long memberId) {
		if(memberId != null) return "redirect:/";
		model.addAttribute("signupRequest", new SignupRequest());
		return "signup";
	}	
	
	
	/*
	 * <회원가입 기능>
	 * 회원가입 데이터를 받았을 때, 다음 경우 중 하나에 해당하면 로그인 페이지로 다시 이동하여 에러 메시지를 출력하게 한다.
	 * (01) 아이디가 빈 문자열일 경우
	 * (02) 아이디 문자열의 길이가 10을 초과할 경우
	 * (03) 이미 존재하는 아이디일 경우
	 * (04) 비밀번호가 빈 문자열일 경우
	 * (05) 비밀번호 문자열의 길이가 10을 초과할 경우
	 * (06) 닉네임이 빈 문자열일 경우
	 * (07) 닉네임의 문자열의 길이가 10을 초과할 경우
	 * 
	 * 위 7가지 경우에 해당하지 않으면 새로운 Member 데이터를 생성하고 이를 db에 저장한 후 메인화면으로 redirect
	 */
	@PostMapping("/signup")
	public String signup(SignupRequest signupRequest, BindingResult result) {
		if(signupRequest.getLoginId().length() == 0) result.addError(new FieldError("signupRequest", "loginId", "아이디를 입력해주세요"));
		else if(signupRequest.getLoginId().length() > 10) result.addError(new FieldError("signupRequest", "loginId", "아이디는 10글자 이하로 입력해주세요."));
		
		if(signupRequest.getPassword().length() == 0) result.addError(new FieldError("signupRequest", "password", "비밀번호를 입력해주세요"));
		else if(signupRequest.getPassword().length() > 10) result.addError(new FieldError("signupRequest", "password", "비밀번호는 10글자 이하로 입력해주세요."));
		
		if(signupRequest.getUsername().length() == 0) result.addError(new FieldError("signupRequest", "username", "닉네임을 입력해주세요"));
		else if(signupRequest.getUsername().length() > 10) result.addError(new FieldError("signupRequest", "username", "닉네임은 10글자 이하로 입력해주세요."));
		
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
	
	
	
	
	
	/*
	 * <글 작성 기능>
	 * 세션 검사를 했을 때 현재 로그인 상태라면 글 작성 페이지로 이동
	 * 세션 검사를 했을 때 현재 로그인 상태가 아니라면 메인화면으로 redirect
	 */
	@GetMapping("/write")
	public String writePost(@SessionAttribute(name = "loginInfo", required = false) Long memberId, Model model) {
		if(memberId == null) return "redirect:/login";	
		Member member = memberService.findById(memberId);
		model.addAttribute("username", member.getUsername());
		model.addAttribute("postCreate", new PostCreate());		
		return "postWrite";
	}
	
	
	/*
	 * <글 작성 기능>
	 * 세션 검사를 했을 때 현재 로그인 상태가 아니라면 메인화면으로 redirect
	 * 
	 * 세션 검사를 했을 때 현재 로그인 상태이고 
	 * 글 작성 데이터를 받았을 때, 다음 경우 중 하나에 해당하면 글 작성 페이지로 다시 이동하여 에러 메시지를 출력하게 한다.
	 * (01) 글 제목이 빈 문자열일 경우
	 * (02) 글 내용이 빈 문자열일 경우
	 * 
	 * 위 2가지 경우에 해당하지 않으면 새로운  Post 데이터를 생성하고 이를 db에 저장한 후 메인화면으로 redirect
	 */
	@PostMapping("/write")
	public String writePost(@SessionAttribute(name = "loginInfo", required = false) Long memberId, 
			PostCreate postCreate, BindingResult result, Model model) {
		if(memberId == null) return "redirect:/";		
		Member member = memberService.findById(memberId);
		
		if(postCreate.getTitle().isEmpty()) result.addError(new FieldError("postCreate", "title", "제목을 입력해주세요"));
		if(postCreate.getContent().isEmpty()) result.addError(new FieldError("postCreate", "content", "내용을 입력해주세요"));
		if(result.hasErrors()) {
			model.addAttribute("username", member.getUsername());
			return "postWrite";
		}
		
		
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
	 * <글 조회 기능> : id로 식별되는 post를 읽어오는 기능
	 * Case1) id로 식별되는 post가 없는 경우
	 * 	Case1-1) 세션 검사를 했을 때 현재 로그인 상태가 아니라면 postNotFound.html 화면을 보여준다.
	 * 	Case1-2) 세션 검사를 했을 때 현재 로그인 상태라면 postNotFoundLoginVersion.html 화면을 보여준다.
	 * 
	 * Case2) id로 식별되는 post가 존재하는 경우
	 * id로 식별되는 post에 대하여 다음 4가지 정보를 받아온다.
	 * 			(1) post의 제목
	 * 			(2) post의 내용
	 * 			(3) post를 수정/삭제할 수 있는 권한이 있는지에 대한 여부
	 * 			(4) post의 댓글정보 List
	 * 			(post의 댓글정보는 댓글의 단 사용자의 username, 댓글의 내용, 댓글을 삭제할 수 있는 권한이 있는지에 대한 여부에 대한 정보가 들어있다.)
	 * 
	 * post를 수정/삭제할 수 있는 권한은 다음과 같다.
	 * 			(1) 세션을 검사했을 때 현재 로그인 상태이며 로그인한 사용자가 post를 작성한 사용자와 동일한 사용자일 때
	 * 			(2) 세션을 검사했을 때 현재 로그인 상태이며 로그인한 사용자의 Grade가 Manager일 때
	 * 
	 * 댓글을 삭제할 수 있는 권한은 다음과 같다.
	 *  		(1) 세션을 검사했을 때 현재 로그인 상태이며 로그인한 사용자가 댓글을 작성한 사용자와 동일한 사용자일 때
	 * 			(2) 세션을 검사했을 때 현재 로그인 상태이며 로그인한 사용자의 Grade가 Manager일 때
	 */
	@GetMapping("/posts/{id}")
	public String getPost(@PathVariable(name = "id") Long postId, Model model, @SessionAttribute(name = "loginInfo", required = false) Long memberId) {		
		Member member = null;
		if(memberId != null) member = memberService.findById(memberId);				
		if(member != null) model.addAttribute("username", member.getUsername());
			
		PostResponse post = postService.getPost(postId, member);
		if(post == null) return memberId == null? "postNotFound" : "postNotFoundLoginVersion";
		
		
		model.addAttribute("post", post);	
		return memberId == null? "postRead" : "postReadLoginVersion";
	}
	
	
	
	
	
	/*
	 * <댓글 작성 기능>
	 * 세션 검사를 했을 때 현재 로그인 상태가 아니라면 메인화면으로 redirect
	 * 세션 검사를 했을 때 현재 로그인 상태이고 댓글 작성 데이터를 받아서 새로운 Comment 데이터를 생성하고 이를 db에 저장한 후 메인화면으로 redirect
	 */
	@PostMapping("/posts/{id}/comment")
	public String createComment(@PathVariable Long id, @SessionAttribute(name = "loginInfo", required = false) Long memberId, CommentCreate commentCreate) {	
		if(memberId == null) return "redirect:/posts/" + id;
		if(memberId != commentCreate.getLoginMemberId()) return "redirect:/posts/" + id;		
		commentService.save(id, commentCreate);
		return "redirect:/posts/" + id;
	}
	
	
	
	
	
	/*
	 * <게시글 수정 기능>
	 * 세션 검사를 했을 때 현재 로그인 상태가 아니라면 메인화면으로 redirect
	 * 세션 검사를 했을 때 현재 로그인 상태라면 글 수정 페이지로 이동
	 */
	@GetMapping("/posts/{id}/edit")
	public String editPost(@PathVariable(name = "id") Long postId, @SessionAttribute(name = "loginInfo", required = false) Long memberId, Model model) {
		if(memberId == null) return "redirect:/"; // 세션을 검사했더니 로그인 상태가 아니면 메인화면으로 돌아가기
		Member member = memberService.findById(memberId);
			
		Post post = postService.findById(postId);
		model.addAttribute("username", member.getUsername());
		if(post == null) return memberId == null? "postNotFound" : "postNotFoundLoginVersion";
									
		PostEdit postEdit = PostEdit.builder()
				.title(post.getTitle())
				.content(post.getContent())
				.build();
		model.addAttribute("postEdit", postEdit);
		return "postEdit";
	}
	
	
	
	/*
	 * <게시글 수정 기능>
	 * 세션 검사를 했을 때 현재 로그인 상태가 아니라면 메인화면으로 redirect
	 * 
	 * 세션 검사를 했을 때 현재 로그인 상태이고 
	 * 글 작성 데이터를 받았을 때, 다음 경우 중 하나에 해당하면 글 작성 페이지로 다시 이동하여 에러 메시지를 출력하게 한다.
	 * (01) 글 제목이 빈 문자열일 경우
	 * (02) 글 내용이 빈 문자열일 경우
	 * 
	 * 위 2가지 경우에 해당하지 않으면 Post 데이터를 수정하고 이를 db에 반영한 후 메인화면으로 redirect
	 */
	@PostMapping("/posts/{id}/edit")
	public String editPost(@PathVariable(name = "id") Long postId, @SessionAttribute(name = "loginInfo", required = false) Long memberId, 
			PostEdit postEdit, BindingResult result, Model model) {		
		if(memberId == null) return "redirect:/"; // 세션을 검사했더니 로그인 상태가 아니면 메인화면으로 돌아가기		
		
		if(postEdit.getTitle().isEmpty()) result.addError(new FieldError("postEdit", "title", "제목을 입력해주세요"));
		if(postEdit.getContent().isEmpty()) result.addError(new FieldError("postEdit", "content", "내용을 입력해주세요"));
		if(result.hasErrors()) {
			Member member = memberService.findById(memberId);
			model.addAttribute("username", member.getUsername());
			return "postEdit";
		}
		
		
		postService.edit(postId, postEdit);
		return "redirect:/posts/" + postId;
	}
	
	
	
	/*
	 * <게시글 삭제 기능>
	 * 세션 검사를 했을 때 현재 로그인 상태가 아니라면 메인화면으로 redirect
	 * 세션 검사를 했을 때 현재 로그인 상태라면 게시글을 삭제하고 이를 db에 반영한 후 메인화면으로 redirect
	 */
	@PostMapping("/posts/{id}/delete")
	public String deletePost(@PathVariable Long id, @SessionAttribute(name = "loginInfo", required = false) Long memberId) {
		if(memberId == null) return "redirect:/";
		postService.deleteById(id);
		return "redirect:/";
	}
	
	
	
	/*
	 * <댓글 삭제 기능>
	 * 세션 검사를 했을 때 현재 로그인 상태가 아니라면 메인화면으로 redirect
	 * 세션 검사를 했을 때 현재 로그인 상태라면 댓글을 삭제하고 이를 db에 반영한 후 원래 해당 댓글이 존재하던 게시글로 redirect
	 */
	@PostMapping("/comments/{commentId}/delete")
	public String deleteComment(@PathVariable Long commentId, @SessionAttribute(name = "loginInfo", required = false) Long memberId, Long postId) {
		if(memberId == null) return "redirect:/";
		commentService.deleteById(commentId);
		return "redirect:/posts/" + postId;
	}
	
	
	
	
	@PostConstruct
	public void init() {
		Member manager = Member.builder().loginId("manager").password("manager").username("manager").build();
		manager.setGrade(Grade.MANAGER);
		memberService.save(manager);
		
		Member[] members = new Member[5];
		Post[] posts = new Post[5];
		CommentCreate[] comments = new CommentCreate[5];
		for(int i = 0; i < 5; i++) {
			String str = "v" + (i + 1);
			members[i] = Member.builder().loginId(str).password(str).username(str).build();
			posts[i] = Post.builder()
					.title("사용자 " + str + " 정보")
					.content("아이디 : " + str + "\n비밀번호 : " + str)
					.member(members[i])
					.build();
			
			memberService.save(members[i]);
			postService.save(posts[i]);
			comments[i] = CommentCreate.builder().loginMemberId(members[i].getId()).content(members[i].getUsername() + "가 작성한 댓글").build();
		}		
		
		for(int i = 0; i < 5; i++)
		for(int j = 0; j < 5; j++)
			commentService.save(posts[i].getId(), comments[j]);
		
		Post post0 = Post.builder()
				.title("운영자 정보")
				.content("아이디 : manager\n비밀번호: manager\n닉네임: manager\n운영자 정보로 로그인 하면 모든 글을 수정/삭제 할 수 있습니다."
						+ "\n또한 모든 댓글을 삭제할 수 있습니다.")
				.member(manager)
				.build();
		postService.save(post0);
	}
}