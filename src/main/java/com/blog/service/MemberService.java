package com.blog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blog.repository.*;
import com.blog.request.LoginRequest;

import lombok.RequiredArgsConstructor;
import com.blog.domain.*;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	
	@Transactional
	public void save(Member member) {
		memberRepository.save(member);
	}
	
	public Long findByLoginId(String loginId) {
		Member member = memberRepository.findByLoginId(loginId);		
		return member == null? null : member.getId();
	}
	
	public Long login(LoginRequest loginRequest) {
		Member member = memberRepository.findByLoginId(loginRequest.getLoginId());		
		if(member == null) return null;
		if(!member.getPassword().equals(loginRequest.getPassword())) return null;
		return member.getId();		
	}
	
	public Member findById(Long id) {
		return memberRepository.findById(id);
	}
}
