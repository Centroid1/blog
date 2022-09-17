package com.blog.request;

import lombok.*;

@Getter @Setter
public class PostEdit {
	private String title;
	private String content;
	
	public PostEdit() {}
	@Builder
	public PostEdit(String title, String content) {
		this.title = title;
		this.content = content;
	}	
}