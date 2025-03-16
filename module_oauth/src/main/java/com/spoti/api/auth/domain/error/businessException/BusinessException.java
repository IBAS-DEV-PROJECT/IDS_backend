package com.spoti.api.auth.domain.error.businessException;

import com.spoti.api.auth.domain.error.ErrorCode;

import lombok.Getter;

/**
 *  부모클래스이자 custonException 클래스
 * 	Service에서 비즈니스를 처리하기 위한 로직이 구현된 부분
**/
@Getter
public class BusinessException extends RuntimeException
{
	private final ErrorCode errorCode;

	public BusinessException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getMessage()); // errorcode 에러 메시지를 반환
		this.errorCode = errorCode;
	}
}
