package com.ssafy.web.review.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.web.review.model.ReviewDto;

@Mapper
public interface ReviewMapper {

	void writeReview(ReviewDto boardDto) throws SQLException;

	List<ReviewDto> listReview(Map<String, Object> param) throws SQLException;

	int getTotalReviewCount(Map<String, Object> param) throws SQLException;

	ReviewDto getReview(int reviewNo) throws SQLException;

	void updateHit(int reviewNo) throws SQLException;

	void modifyReview(ReviewDto boardDto) throws SQLException;

	void deleteReview(int reviewNo) throws SQLException;

}