package com.ssafy.web.map.model;

import com.ssafy.web.exception.ExceptionCode;
import com.ssafy.web.exception.MyException;

public class ListParamDto {

	private int sidoCode;
	private int gugunCode;
	private String contentTypeId = "";
	private String key = "";
	private String word = "";

	public int getSidoCode() {
		return sidoCode;
	}

	public void setSidoCode(int sidoCode) {
		if (!((0 <= sidoCode && sidoCode <= 8) || (31 <= sidoCode && sidoCode <= 39))) {
			throw new MyException(ExceptionCode.SIDOCODE_INCORRECT);
		}
		this.sidoCode = sidoCode;
	}

	public int getGugunCode() {
		return gugunCode;
	}

	public void setGugunCode(int gugunCode) {
		if (gugunCode < 0 || gugunCode > 31) {
			throw new MyException(ExceptionCode.GUGUNCODE_INCORRECT);
		}
		this.gugunCode = gugunCode;
	}

	public String getContentTypeId() {
		return contentTypeId;
	}

	public void setContentTypeId(String contentTypeId) {
		if (contentTypeId == null) {
			throw new MyException(ExceptionCode.SETTER_NOT_NULL);
		}
		this.contentTypeId = contentTypeId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		if (key == null) {
			throw new MyException(ExceptionCode.SETTER_NOT_NULL);
		}
		if (!("".equals(key) || "title".equals(key) || "addr1".equals(key))) {
			throw new MyException(ExceptionCode.KEY_INCORRECT);
		}
		this.key = key;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		if (word == null) {
			throw new MyException(ExceptionCode.SETTER_NOT_NULL);
		}
		this.word = word;
	}

	@Override
	public String toString() {
		return "ListParamDto [sidoCode=" + sidoCode + ", gugunCode=" + gugunCode + ", contentTypeId=" + contentTypeId
				+ ", key=" + key + ", word=" + word + "]";
	}

}
