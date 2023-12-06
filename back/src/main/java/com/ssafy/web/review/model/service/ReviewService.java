package com.ssafy.web.review.model.service;

import java.util.Map;

import com.ssafy.web.review.model.ReviewDto;
import com.ssafy.web.review.model.ReviewListDto;

public interface ReviewService {

	void writeReview(ReviewDto reviewDto) throws Exception;

	ReviewListDto listReview(Map<String, String> map) throws Exception;

	ReviewDto getReview(int reviewNo) throws Exception;

	void updateHit(int reviewNo) throws Exception;

	void modifyReview(ReviewDto reviewDto) throws Exception;

	void deleteReview(int reviewNo) throws Exception;

}
