package com.ssafy.web.map.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.web.map.model.AttractionDto;
import com.ssafy.web.map.model.GugunDto;
import com.ssafy.web.map.model.ListParamDto;
import com.ssafy.web.map.model.SidoDto;
import com.ssafy.web.map.model.mapper.MapMapper;

@Service
public class MapServiceImpl implements MapService {
	
	private MapMapper mapMapper;

	public MapServiceImpl(MapMapper mapMapper) {
		super();
		this.mapMapper = mapMapper;
	}

	@Override
	public List<SidoDto> getSido() throws Exception {
		return mapMapper.getSido();
	}

	@Override
	public List<GugunDto> getGugunInSido(int sido) throws Exception {
		return mapMapper.getGugunInSido(sido);
	}

	@Override
	public List<AttractionDto> listAttraction(ListParamDto param) {
		param.setWord(param.getWord().trim());
		return mapMapper.listAttraction(param);
	}

	@Override
	public String detailAttraction(String contentId) {
		return mapMapper.detailAttraction(contentId);
	}

}
