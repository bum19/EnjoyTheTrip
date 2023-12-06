package com.ssafy.web.exception;

public class MyException extends RuntimeException {

	private ExceptionCode code;

	public MyException(ExceptionCode code) {
		super(code.getMessage());
		this.code = code;
	}

	public ExceptionCode getCode() {
		return code;
	}

}
