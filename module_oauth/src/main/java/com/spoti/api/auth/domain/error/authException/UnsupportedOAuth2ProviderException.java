package com.spoti.api.auth.domain.error.authException;

import com.spoti.api.auth.domain.error.ErrorCode;

public class UnsupportedOAuth2ProviderException extends CustomAuthException {

	public UnsupportedOAuth2ProviderException() {
		super(ErrorCode.UNSUPPORTED_OAUTH2_PROVIDER);
	}
}
