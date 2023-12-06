package com.ssafy.web.member.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.web.member.model.LoginCheckDto;
import com.ssafy.web.member.model.MemberDto;

@Mapper
public interface MemberMapper {

	int emailCheck(String email) throws SQLException;
	void joinMember(MemberDto memberDto);
	MemberDto loginMember(MemberDto memberDto) throws SQLException;
	
	/* Admin */
	List<MemberDto> listMember(Map<String, Object> map) throws SQLException;
	MemberDto getMember(String email) throws SQLException;
	void updateMember(MemberDto memberDto) throws SQLException;
	void deleteMember(String email) throws SQLException;
	LoginCheckDto loginCheck(String email) throws SQLException;
	void deleteLoginCheck(String email) throws SQLException;
	void plusLoginCount(String email) throws SQLException;

	String getQuestion(String email);
	void updatePW(MemberDto memberDto) throws SQLException;
	
}
