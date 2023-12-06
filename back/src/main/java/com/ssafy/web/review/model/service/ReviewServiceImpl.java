package com.ssafy.web.review.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.web.map.model.mapper.MapMapper;
import com.ssafy.web.review.model.ReviewDto;
import com.ssafy.web.review.model.ReviewListDto;
import com.ssafy.web.review.model.mapper.ReviewMapper;

@Service
public class ReviewServiceImpl implements ReviewService {

	private final ReviewMapper reviewMapper;
	private final MapMapper mapMapper;

	@Autowired
	public ReviewServiceImpl(ReviewMapper reviewMapper, MapMapper mapMapper) {
		super();
		this.reviewMapper = reviewMapper;
		this.mapMapper = mapMapper;
	}

	@Override
	@Transactional
	public void writeReview(ReviewDto reviewDto) throws Exception {
		System.out.println("글입력 전 dto : " + reviewDto);

		// 해당 여행지 있는지검증필요! db접근해서
//		if(mapMapper.findById(reviewDto.getContentId()) == 0){
//			throws new Exception("no 해당 여행지");
//		}

		reviewMapper.writeReview(reviewDto);
		System.out.println("글입력 후 dto : " + reviewDto);
	}

	@Override
	public ReviewListDto listReview(Map<String, String> map) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		String key = map.get("key");

		// 키값이 작성자 아이디일경우 key값 변경. //키값이 널일경우 ""값으로 바꾸기.
		param.put("key", key == null ? "" : key);
		if ("email".equals(param.get("key")))
			param.put("key", "email");
		if ("reviewNo".equals(param.get("key")))
			param.put("key", "review_no");
		// 키워드 값이 널일경우 ""값으로 바꾸기.
		param.put("word", map.get("word") == null ? "" : map.get("word"));

		// 입력받은 페이지 정보
		int currentPage = Integer.parseInt(map.get("pgno") == null ? "1" : map.get("pgno"));
		int sizePerPage = Integer.parseInt(map.get("spp") == null ? "20" : map.get("spp"));

		// 실제 시작 위치
		int start = currentPage * sizePerPage - sizePerPage;

		// 받아올 시작 위치. 1페이지 -> 0페이지로.
		param.put("start", start);
		param.put("listsize", sizePerPage);

		// SQL문 조회.
		System.out.println("쿼리날리기전 param 체크 : " + param);
		List<ReviewDto> list = reviewMapper.listReview(param);

		// 총 리뷰개수 조회
		int totalReviewCount = reviewMapper.getTotalReviewCount(param);
		int totalPageCount = (totalReviewCount - 1) / sizePerPage + 1;

		// DTO에 페이지정보와 리스트 담아서 보내기
		ReviewListDto reviewListDto = new ReviewListDto();
		reviewListDto.setReviews(list);
		reviewListDto.setCurrentPage(currentPage);
		reviewListDto.setTotalPageCount(totalPageCount);

		return reviewListDto;
	}

	@Override
	public ReviewDto getReview(int reviewNo) throws Exception {
		return reviewMapper.getReview(reviewNo);
	}

	@Override
	public void updateHit(int reviewNo) throws Exception {
		reviewMapper.updateHit(reviewNo);
	}

	@Override
	public void modifyReview(ReviewDto reviewDto) throws Exception {
		reviewMapper.modifyReview(reviewDto);
		// 추후 파일 수정 구현
	}

	@Override
	@Transactional
	public void deleteReview(int reviewNo) throws Exception {
		reviewMapper.deleteReview(reviewNo);
	}

}
