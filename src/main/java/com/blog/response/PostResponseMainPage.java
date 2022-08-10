package com.blog.response;

import lombok.*;


@Getter @Setter
public class PostResponseMainPage {
	private Long postId;
	private String title;
	private String username;
	private String createdTime;
	
	@Builder
	public PostResponseMainPage(Long postId, String title, String username, String createdTime) {
		this.postId = postId;
		this.title = title;
		this.username = username;
		this.createdTime = createdTime;
	}
}