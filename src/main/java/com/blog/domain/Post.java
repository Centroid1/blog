package com.blog.domain;

import javax.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Getter @Setter
@SequenceGenerator(name = "NUM", initialValue = 1, allocationSize = 1)
public class Post {
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NUM")
	private Long id;
	
	String title;
	@Lob private String content;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID")
	private Member member;
	
	private String createdTime;
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
	private List<Comment> comments = new ArrayList<>();
	
	public Post() {}
	@Builder
	public Post(String title, String content, Member member) {
		this.title = title;
		this.content = content;
		this.member = member;
		this.createdTime = Timer.getTime();
	}
}
