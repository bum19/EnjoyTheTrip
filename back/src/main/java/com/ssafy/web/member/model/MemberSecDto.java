package com.ssafy.web.member.model;

import com.ssafy.web.exception.ExceptionCode;
import com.ssafy.web.exception.MyException;

public class MemberSecDto {
	private String email;
	private String salt;
	private String secKey;
	private String initVector;

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

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		if (salt == null) {
			throw new MyException(ExceptionCode.SETTER_NOT_NULL);
		}
		if (salt.length() > 100) {
			throw new MyException(ExceptionCode.SALT_LENGTH_TOO_LONG);
		}
		this.salt = salt;
	}

	public String getSecKey() {
		return secKey;
	}

	public void setSecKey(String secKey) {
		if (secKey == null) {
			throw new MyException(ExceptionCode.SETTER_NOT_NULL);
		}
		if (secKey.length() > 100) {
			throw new MyException(ExceptionCode.SECKEY_LENGTH_TOO_LONG);
		}
		this.secKey = secKey;
	}

	public String getInitVector() {
		return initVector;
	}

	public void setInitVector(String initVector) {
		if (initVector == null) {
			throw new MyException(ExceptionCode.SETTER_NOT_NULL);
		}
		if (initVector.length() > 100) {
			throw new MyException(ExceptionCode.INITVECTOR_LENGTH_TOO_LONG);
		}
		this.initVector = initVector;
	}

	@Override
	public String toString() {
		return "MemberSecDto [email=" + email + ", salt=" + salt + ", secKey=" + secKey + ", initVector=" + initVector
				+ "]";
	}

}
