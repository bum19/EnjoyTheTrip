package com.ssafy.web.review.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ssafy.web.exception.MyException;

@ControllerAdvice
public class ExceptionControllerAdvice {

	private final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

//	@ExceptionHandler(Exception.class)
//	public ResponseEntity<String> handleException(Exception ex) {
//		logger.error("Exception 발생 : {}", ex.getMessage());
//		return new ResponseEntity<String>("처리중 에러 발생!!!", HttpStatus.valueOf(400));
//	}

	@ExceptionHandler(MyException.class)
	public ResponseEntity<String> handleMyException(MyException ex) {
		logger.error("Exception 발생 : {}", ex.getMessage());
		return new ResponseEntity<String>(ex.getCode().getMessage(), HttpStatus.valueOf(ex.getCode().getStatus()));
	}

}
