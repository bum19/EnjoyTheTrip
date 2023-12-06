package com.ssafy.web.review.model;

import com.ssafy.web.exception.ExceptionCode;
import com.ssafy.web.exception.MyException;

public class ReviewDto {

	private int reviewNo;
	private String email;
	private String subject;
	private String content;
	private int hit;
	private int score;
	private int contentId;
	private String attractionTitle;
	private String registerTime;

	public int getReviewNo() {
		return reviewNo;
	}

	public void setReviewNo(int reviewNo) {
		if (reviewNo < 1) {
			throw new MyException(ExceptionCode.SETTER_NOT_NULL);
		}
		this.reviewNo = reviewNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (email == null) {
			throw new MyException(ExceptionCode.SETTER_NOT_NULL);
		}
		if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")) {
			throw new MyException(ExceptionCode.EMAIL_BAD_REQUEST);
		}
		if (email.length() > 50) {
			throw new MyException(ExceptionCode.EMAIL_LENGTH_TOO_LONG);
		}
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		if (subject == null) {
			throw new MyException(ExceptionCode.SETTER_NOT_NULL);
		}
		if (subject.length() > 100) {
			throw new MyException(ExceptionCode.SUBJECT_LENGTH_TOO_LONG);
		}
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		if (content == null) {
			throw new MyException(ExceptionCode.SETTER_NOT_NULL);
		}
		if (content.length() > 2000) {
			throw new MyException(ExceptionCode.CONTENT_LENGTH_TOO_LONG);
		}
		this.content = content;
	}

	public int getHit() {
		return hit;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		if (score < 1 || score > 5) {
			throw new MyException(ExceptionCode.SCORE_RANGE_INCORRECT);
		}
		this.score = score;
	}

	public int getContentId() {
		return contentId;
	}

	public void setContentId(int contentId) {
		if (contentId < 1) {
			throw new MyException(ExceptionCode.SETTER_NOT_NULL);
		}
		this.contentId = contentId;
	}

	public String getAttractionTitle() {
		return attractionTitle;
	}

	public void setAttractionTitle(String attractionTitle) {
		if (attractionTitle == null) {
			throw new MyException(ExceptionCode.SETTER_NOT_NULL);
		}
		if (attractionTitle.length() > 100) {
			throw new MyException(ExceptionCode.ATTRACTIONTITLE_LENGTH_TOO_LONG);
		}
		this.attractionTitle = attractionTitle;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	@Override
	public String toString() {
		return "ReviewDto [reviewNo=" + reviewNo + ", email=" + email + ", subject=" + subject + ", content=" + content
				+ ", hit=" + hit + ", score=" + score + ", contentId=" + contentId + ", attractionTitle="
				+ attractionTitle + ", registerTime=" + registerTime + "]";
	}

}
