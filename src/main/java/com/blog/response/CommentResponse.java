package com.blog.response;

import lombok.*;

@Getter @Setter
public class CommentResponse {
	private Long commentId;
	private Long memberId;
	private String username;
	private String content;
	private boolean commentAuthority;
		
	@Builder
	public CommentResponse(Long commentId, Long memberId, String username, String content, boolean commentAuthority) {
		this.commentId = commentId;
		this.memberId = memberId;
		this.username = username;
		this.content = content;
		this.commentAuthority = commentAuthority;
	}
}
