package com.ssafy.web.map.model.service;

import java.util.List;

import com.ssafy.web.map.model.AttractionDto;
import com.ssafy.web.map.model.GugunDto;
import com.ssafy.web.map.model.ListParamDto;
import com.ssafy.web.map.model.SidoDto;

public interface MapService {

	List<SidoDto> getSido() throws Exception;
	List<GugunDto> getGugunInSido(int sido) throws Exception;
	List<AttractionDto> listAttraction(ListParamDto param);
	String detailAttraction(String contentId);

}
