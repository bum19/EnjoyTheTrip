package com.ssafy.web.member.sec.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.ssafy.web.member.model.MemberSecDto;

@Mapper
@Repository
public interface MemberSecMapper {
	public void join(MemberSecDto memberSecDto);
	public  MemberSecDto find(String email);
	public void delete(String email);
	public void update(MemberSecDto memberSecDto);
}
