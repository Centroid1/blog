package com.blog.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blog.domain.Post;
import com.blog.repository.*;
import com.blog.request.PostCreate;
import com.blog.request.PostEdit;
import com.blog.response.*;
import lombok.RequiredArgsConstructor;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
	private final PostRepository postRepository;
	
	public List<PostResponseMainPage> findAll(Pageable pageable){		
		return postRepository.findAll(pageable)
				.stream()
				.map(x -> PostResponseMainPage.builder()
						.postId(x.getId())
						.title(x.getTitle())
						.username(x.getMember().getUsername())
						.createdTime(x.getCreatedTime())
						.build()).collect(Collectors.toList());
	}
	
	@Transactional
	public void save(Post post) {
		postRepository.save(post);
	}
	
	@Transactional
	public void save(PostCreate postCreate) {
		postRepository.save(Post.builder()
				.title(postCreate.getTitle())
				.content(postCreate.getContent())
				.build());
	}
	
	@Transactional
	public void edit(Long id, PostEdit postEdit) {
		Post post = postRepository.findById(id).get();
		post.setTitle(postEdit.getTitle());
		post.setContent(postEdit.getContent());
	}
	
	
	
	public Post findById(Long id) {
		return postRepository.findById(id).orElse(null);
	}
	
	@Transactional
	public void deleteById(Long id) {
		postRepository.deleteById(id);
	}
}
