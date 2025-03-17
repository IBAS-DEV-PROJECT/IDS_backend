package com.spoti.api.auth.domain.oauth2.socialShare.domain;

import java.io.Serial;

/**
 * 컨텐츠를 찾을 수 없을 때 발생하는 예외
 */
public class ContentNotFoundException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	public ContentNotFoundException(String message) {
		super(message);
	}

	public ContentNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
