package com.blog.request;

import lombok.*;

@Getter @Setter
public class PostCreate {
	private String title;
	private String content;
	
	public PostCreate() {}
	@Builder
	public PostCreate(String title, String content) {
		this.title = title;
		this.content = content;
	}	
}