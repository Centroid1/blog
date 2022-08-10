package com.blog.response;

import lombok.*;

@Getter @Setter
public class CommentResponse {
	private Long commentId;
	private String username;
	private String content;
	private boolean commentAuthority;
	
	@Builder
	public CommentResponse(Long commentId, String username, String content, boolean commentAuthority) {
		this.commentId = commentId;
		this.username = username;
		this.content = content;
		this.commentAuthority = commentAuthority;
	}
}
