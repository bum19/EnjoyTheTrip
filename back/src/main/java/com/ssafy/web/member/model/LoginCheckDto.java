package com.ssafy.web.member.model;

import java.util.Date;

import com.ssafy.web.exception.ExceptionCode;
import com.ssafy.web.exception.MyException;

public class LoginCheckDto {
	private String email;
	private int tryCount;
	private Date lastTryTime;

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

	public int getTryCount() {
		return tryCount;
	}

	public void setTryCount(int tryCount) {
		if (tryCount < 1 || tryCount > 5) {
			throw new MyException(ExceptionCode.SETTER_NOT_NULL);
		}
		this.tryCount = tryCount;
	}

	public Date getLastTryTime() {
		return lastTryTime;
	}

	public void setLastTryTime(Date lastTryTime) {
		if (lastTryTime == null) {
			throw new MyException(ExceptionCode.SETTER_NOT_NULL);
		}
		this.lastTryTime = lastTryTime;
	}

	@Override
	public String toString() {
		return "LoginCheckDto [email=" + email + ", tryCount=" + tryCount + ", lastTryTime=" + lastTryTime + "]";
	}

}
