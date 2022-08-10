package com.blog.request;

import lombok.*;

@Getter @Setter
public class LoginRequest {
	private String loginId;
	private String password;
	
	public LoginRequest() {}
	public LoginRequest(String loginId, String password) {
		this.loginId = loginId;
		this.password = password;
	}
}
