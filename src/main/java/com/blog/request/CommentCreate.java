package com.blog.request;

import lombok.*;

@Getter @Setter
public class CommentCreate {
	private Long loginMemberId;
	private String content;
	
	@Builder
	public CommentCreate(Long loginMemberId, String content) {
		this.loginMemberId = loginMemberId;
		this.content = content;
	}
}
