package com.blog.domain;

import javax.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Comment {
	@Id @GeneratedValue
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID")
	private Member member;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POST_ID")
	private Post post;	
	private String content;
	
	@Builder
	public Comment(Member member, Post post, String content) {
		this.member = member;
		this.post = post;
		this.content = content;
	}
}