package com.ssafy.web.map.model.mapper;

import java.sql.SQLException;
import java.util.List;

import com.ssafy.web.map.model.AttractionDto;
import com.ssafy.web.map.model.GugunDto;
import com.ssafy.web.map.model.ListParamDto;
import com.ssafy.web.map.model.SidoDto;

public interface MapMapper {

	List<SidoDto> getSido() throws SQLException;
	List<GugunDto> getGugunInSido(int sido) throws SQLException;
	List<AttractionDto> listAttraction(ListParamDto param);
	String detailAttraction(String contentId);
	
}
