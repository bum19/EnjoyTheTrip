package com.ssafy.web.review.model;

import java.util.List;

import com.ssafy.web.exception.ExceptionCode;
import com.ssafy.web.exception.MyException;

public class ReviewListDto {

	private List<ReviewDto> reviews;
	private int currentPage;
	private int totalPageCount;

	public List<ReviewDto> getReviews() {
		return reviews;
	}

	public void setReviews(List<ReviewDto> reviews) {
		if (reviews == null) {
			throw new MyException(ExceptionCode.SETTER_NOT_NULL);
		}
		this.reviews = reviews;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		if (currentPage < 1) {
			throw new MyException(ExceptionCode.SETTER_NOT_NULL);
		}
		this.currentPage = currentPage;
	}

	public int getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(int totalPageCount) {
		if (totalPageCount < 1) {
			throw new MyException(ExceptionCode.SETTER_NOT_NULL);
		}
		this.totalPageCount = totalPageCount;
	}

	@Override
	public String toString() {
		return "ReviewListDto [reviews=" + reviews + ", currentPage=" + currentPage + ", totalPageCount="
				+ totalPageCount + "]";
	}

}
