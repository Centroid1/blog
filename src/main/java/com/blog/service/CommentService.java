package com.blog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blog.repository.*;
import com.blog.request.CommentCreate;
import com.blog.domain.*;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final MemberRepository memberRepository;
	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	
	@Transactional
	public void save(Long postId, CommentCreate commentCreate) {
		Member member = memberRepository.findById(commentCreate.getLoginMemberId());
		Post post = postRepository.findById(postId).get();
		Comment comment = Comment.builder()
							.member(member)
							.post(post)
							.content(commentCreate.getContent())
							.build();
		
		commentRepository.save(comment);
		member.getComments().add(comment);
		post.getComments().add(comment);		
	}	
	
	@Transactional
	public void deleteById(Long id) {
		commentRepository.deleteById(id);
	}
}
