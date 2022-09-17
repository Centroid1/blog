package com.blog.repository;

import javax.persistence.*;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.stream.Collectors;
import lombok.*;
import com.blog.response.*;
import com.blog.domain.*;

@Repository
@RequiredArgsConstructor
public class PostRepositoryJPQL {
	private final EntityManager em;
	
	public List<PostResponseMainPage> f(){
		List<Post> posts = em.createQuery("select p from Post p join fetch p.member m order by p.id desc", Post.class)
				.setFirstResult(0).setMaxResults(10).getResultList();
		return posts.stream().map(x -> PostResponseMainPage.builder()
										.postId(x.getId())
										.title(x.getTitle())
										.username(x.getMember().getUsername())
										.createdTime(x.getCreatedTime())
										.build()).collect(Collectors.toList());
	}
	public List<PostResponseMainPage> findAllMainPage(int page){
		List<Post> posts = em.createQuery("select p from Post p join fetch p.member m order by p.id desc", Post.class)
				.setFirstResult(page * 10).setMaxResults(10).getResultList();
		return posts.stream().map(x -> PostResponseMainPage.builder()
														   .postId(x.getId())
														   .title(x.getTitle())
														   .username(x.getMember().getUsername())
														   .createdTime(x.getCreatedTime())
														   .build()).collect(Collectors.toList());
	}
	
	public PostResponse getPost(Long id, Member member) {	
		Post post = em.find(Post.class, id);
		if(post == null) return null;
		Long memberId = (member == null? null : member.getId());
		
		PostResponse postResponse = PostResponse.builder()
				.postId(post.getId())
				.loginMemberId(member == null? null : member.getId())
				.title(post.getTitle())
				.content(post.getContent())
				.postAuthority(post.getMember().getId() == memberId || (member != null && member.getGrade() == Grade.MANAGER))
				.build();		
		
		List<CommentResponse> comments = em.createQuery("select new com.blog.response.CommentResponse("
				+ "c.id, m.id, m.username, c.content, false) from Post p "
				+ "join p.comments c join c.member m where p.id = :id", CommentResponse.class)
				.setParameter("id", id).getResultList();
		for(CommentResponse comment : comments) 
			comment.setCommentAuthority(comment.getMemberId() == memberId || (member != null && member.getGrade() == Grade.MANAGER));
		
		
		postResponse.setComments(comments);
		return postResponse;
	}
}
