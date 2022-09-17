package com.blog.request;

import lombok.*;

@Getter @Setter
public class SignupRequest {
	private String loginId;
	private String password;
	private String username;	
	
	public SignupRequest() {}
	public SignupRequest(String loginId, String password, String username) {
		this.loginId = loginId;
		this.password = password;
		this.username = username;
	}
}
