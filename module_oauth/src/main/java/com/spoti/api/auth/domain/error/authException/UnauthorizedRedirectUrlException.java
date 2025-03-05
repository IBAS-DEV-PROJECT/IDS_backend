package com.spoti.api.auth.domain.error.authException;

import com.spoti.api.auth.domain.error.ErrorCode;

public class UnauthorizedRedirectUrlException extends CustomAuthException {

	public UnauthorizedRedirectUrlException() {
		super(ErrorCode.UNAUTHORIZED_REDIRECT_URI);
	}
}
