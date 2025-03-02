package com.spoti.api.auth.domain.error.authException;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;
import com.spoti.api.auth.domain.error.ErrorCode;

/**
 * {@code auth-module} 에서 발생하는 오류들은 {@code CustomAuthException} 을 상속받아서 구현해야함. 특히 spring security
 * filter에서 발생하는 오류들을 처리하기 위해서는 필수적으로 {@code AuthenticationException} 을 상속받아야함.
 */
@Getter
public abstract class CustomAuthException extends AuthenticationException {

	private final ErrorCode errorCode;
	//오류 메시지 + 오류 코드 받는 경우
	public CustomAuthException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
	//오류 코드 받는 경우
	public CustomAuthException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
