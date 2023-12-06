package com.ssafy.web.map.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.web.map.model.AttractionDto;
import com.ssafy.web.map.model.GugunDto;
import com.ssafy.web.map.model.ListParamDto;
import com.ssafy.web.map.model.SidoDto;
import com.ssafy.web.map.model.service.MapService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/map")
@Slf4j
public class MapController {

	private MapService mapService;

	public MapController(MapService mapService) {
		super();
		this.mapService = mapService;
	}

	@GetMapping("/sido")
	public ResponseEntity<List<SidoDto>> sido() throws Exception {
		log.info("sido - 호출");
		return new ResponseEntity<List<SidoDto>>(mapService.getSido(), HttpStatus.OK);
	}

	@GetMapping("/gugun")
	public ResponseEntity<List<GugunDto>> gugun(@RequestParam("sido") int sido) throws Exception {
		log.info("gugun - 호출");
		return new ResponseEntity<List<GugunDto>>(mapService.getGugunInSido(sido), HttpStatus.OK);
	}

	@PostMapping("/attractions")
	public ResponseEntity<List<AttractionDto>> listAttraction(@RequestBody ListParamDto listParam) {
		System.out.println(listParam);
		List<AttractionDto> list = mapService.listAttraction(listParam);
		return new ResponseEntity<List<AttractionDto>>(list, HttpStatus.OK);
	}

	@GetMapping("/attractions/detail")
	public ResponseEntity<String> detailAttraction(@RequestParam String contentId) {
		System.out.println("contentId:" + contentId);
		String overview = mapService.detailAttraction(contentId);
		return new ResponseEntity<String>(overview, HttpStatus.OK);
	}

}
