package com.blog.response;

import lombok.*;
import java.util.*;

@Getter @Setter
public class PostResponse {
	private Long postId;
	private Long loginMemberId;
	private String title;
	private String content;
	private boolean postAuthority;	
	private List<CommentResponse> comments = new ArrayList<>();
	
	@Builder
	public PostResponse(Long postId, Long loginMemberId, String title, String content, boolean postAuthority) {
		this.postId = postId;
		this.loginMemberId = loginMemberId;
		this.title = title;
		this.content = content;
		this.postAuthority = postAuthority;
	}
}