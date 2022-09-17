package com.blog.repository;

import java.util.Optional;

import javax.persistence.*;
import org.springframework.stereotype.Repository;
import lombok.*;
import com.blog.domain.*;

@RequiredArgsConstructor
@Repository
public class MemberRepository {
	private final EntityManager em;
	
	public void save(Member member) {
		em.persist(member);
	}
	
	public Member findByLoginId(String loginId) {
		Optional<Member> m = em.createQuery("select m from Member m where m.loginId = :loginId", Member.class)
				.setParameter("loginId", loginId).getResultStream().findAny();
		
		if(m.isEmpty()) return null;		
		return m.get();
	}
	public Member findById(Long id) {
		return em.find(Member.class, id);
	}
}
