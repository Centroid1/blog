package com.blog.domain;

import lombok.*;
import javax.persistence.*;
import java.util.*;

@Entity
@Getter @Setter
@Table(name = "MEMBER", uniqueConstraints = { @UniqueConstraint(name = "LOGIN_ID_UNIQUE", columnNames = {"LOGIN_ID"})})
public class Member {
	@Id @GeneratedValue
	private Long id;
	
	@Column(name = "LOGIN_ID")
	private String loginId;
	private String password;
	private String username;
	
	@Enumerated(EnumType.STRING)
	private Grade grade;
	
	@OneToMany(mappedBy = "member")
	private List<Post> posts = new ArrayList<>();
	
	@OneToMany(mappedBy = "member")
	private List<Comment> comments = new ArrayList<>();
	
	public Member() {}
	@Builder
	public Member(String loginId, String password, String username) {
		this.loginId = loginId;
		this.password = password;
		this.username = username;
		this.grade = Grade.USER;
	}
}
