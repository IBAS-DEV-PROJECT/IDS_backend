package com.spoti.api.auth.domain.error.businessException;

import com.spoti.api.auth.domain.error.ErrorCode;

public class InternalServerException extends BusinessException {

	public InternalServerException() {
		super(ErrorCode.INTERNAL_SERVER_ERROR);
	}
}
