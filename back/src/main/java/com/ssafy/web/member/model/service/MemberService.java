package com.ssafy.web.member.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.web.member.model.MemberDto;

public interface MemberService {

	int emailCheck(String email) throws Exception;
	void joinMember(MemberDto memberDto) throws Exception;
	MemberDto loginMember(MemberDto memberDto) throws Exception;
	
	/* Admin */
	List<MemberDto> listMember(Map<String, Object> map) throws Exception;
	MemberDto getMember(String email) throws Exception;
	void updateMember(MemberDto memberDto) throws Exception;
	void deleteMember(String email) throws Exception;

	String getQuestion(String email);
	boolean updatePW(MemberDto memberDto) throws Exception;
	boolean loginCheck(String email) throws Exception;
}