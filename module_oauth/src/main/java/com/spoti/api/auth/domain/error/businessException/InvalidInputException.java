package com.spoti.api.auth.domain.error.businessException;

import com.spoti.api.auth.domain.error.ErrorCode;

public class InvalidInputException extends BusinessException {
	public InvalidInputException() {
		super(ErrorCode.INVALID_INPUT_VALUE);
	}
}
