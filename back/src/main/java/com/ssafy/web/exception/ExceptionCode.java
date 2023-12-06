package com.ssafy.web.exception;

public enum ExceptionCode {
	SETTER_NOT_NULL(400, "너 해커야?"),
	EMAIL_BAD_REQUEST(400, "이메일 양식을 준수해주세요."),
	EMAIL_LENGTH_TOO_LONG(400, "이메일이 너무 길어요."),
	USERNAME_LENGTH_TOO_LONG(400, "이름이 너무 길어요."),
	QUESTION_LENGTH_TOO_LONG(400, "질문이 너무 길어요."),
	SCORE_RANGE_INCORRECT(400, "점수 범위가 잘못됐어요."),
	SUBJECT_LENGTH_TOO_LONG(400, "제목이 너무 길어요."),
	CONTENT_LENGTH_TOO_LONG(400, "내용이 너무 많아요."),
	ATTRACTIONTITLE_LENGTH_TOO_LONG(400, "여행지 이름이 길 수가 없는데???"),
	SALT_LENGTH_TOO_LONG(400, "소금이 너무 짜요."),
	SECKEY_LENGTH_TOO_LONG(400, "비밀키가 너무 길어요."),
	INITVECTOR_LENGTH_TOO_LONG(400, "시작 벡터가 너무 길어요."),
	SIDOCODE_INCORRECT(400, "시도 코드가 잘못됐어요."),
	GUGUNCODE_INCORRECT(400, "구군 코드가 잘못됐어요."),
	KEY_INCORRECT(400, "검색 조건이 잘못됐어요."),
	EMAIL_EXIST(400, "이미 존재하는 이메일입니다."),
	;
 
    private int status;

    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
    
}