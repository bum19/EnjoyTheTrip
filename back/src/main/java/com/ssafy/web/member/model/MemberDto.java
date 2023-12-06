package com.ssafy.web.member.model;

import java.sql.Date;

import com.ssafy.web.exception.ExceptionCode;
import com.ssafy.web.exception.MyException;

public class MemberDto {

	private String email;
	private String userName;
	private String userPassword;
	private String question;
	private String answer;
	private Date joinDate;

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		if (userName == null || userName.equals("")) {
			throw new MyException(ExceptionCode.SETTER_NOT_NULL);
		}
		if (userName.length() > 20) {
			throw new MyException(ExceptionCode.USERNAME_LENGTH_TOO_LONG);
		}
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		if (userPassword == null || userPassword.equals("")) {
			throw new MyException(ExceptionCode.SETTER_NOT_NULL);
		}
		this.userPassword = userPassword;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		if (question == null || question.equals("")) {
			throw new MyException(ExceptionCode.SETTER_NOT_NULL);
		}
		if (question.length() > 50) {
			throw new MyException(ExceptionCode.QUESTION_LENGTH_TOO_LONG);
		}
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		if (answer == null || answer.equals("")) {
			throw new MyException(ExceptionCode.SETTER_NOT_NULL);
		}
		this.answer = answer;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		if (joinDate == null) {
			throw new MyException(ExceptionCode.SETTER_NOT_NULL);
		}
		this.joinDate = joinDate;
	}

	@Override
	public String toString() {
		return "MemberDto [email=" + email + ", userName=" + userName + ", userPassword=" + userPassword + ", question="
				+ question + ", answer=" + answer + ", joinDate=" + joinDate + "]";
	}

}
