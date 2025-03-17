package com.spoti.api.auth.domain.token.exception;

import com.spoti.api.auth.domain.error.ErrorCode;
import com.spoti.api.auth.domain.error.authException.CustomAuthException;

public class TokenMissingException extends CustomAuthException {

	//LoginMemberArgumentResolver에서 사용 구현해야함
	public TokenMissingException() {
		super(ErrorCode.JWT_MISSING);
	}
}
