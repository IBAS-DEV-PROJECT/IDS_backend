package com.spoti.api.auth.domain.token.exception;

import com.spoti.api.auth.domain.error.ErrorCode;
import com.spoti.api.auth.domain.error.authException.CustomAuthException;

public class ExpiredTokenException extends CustomAuthException {

	public ExpiredTokenException() {
		super(ErrorCode.JWT_EXPIRED);
	}
}
