package com.spoti.api.auth.domain.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
//예시이며 추후에 수정해야됨
@Getter
@AllArgsConstructor
public enum ErrorCode {

	// Global
	INTERNAL_SERVER_ERROR(500, "G001", "내부 서버 오류입니다."),
	METHOD_NOT_ALLOWED(405, "G002", "허용되지 않은 HTTP method입니다."),
	INVALID_INPUT_VALUE(400, "G003", "입력값이 없거나, 타입이 유효하지 않습니다."),
	NOT_FOUND(404, "G004", "데이터가 존재하지 않습니다."),
	UNSUPPORTED_MEDIA_TYPE(415, "G005", "지원되지 않는 형식입니다."),

	// Auth (ExceptionHandler 불필요)
	/**
	 * The value of one or more redirection URIs is unauthorized.
	 */
	UNAUTHORIZED_REDIRECT_URI(401, "A001", "유효하지 않은 redirect_uri 입니다."),
	/**
	 * 지원하지 않는 소셜로그인
	 */
	UNSUPPORTED_OAUTH2_PROVIDER(401, "A002", "지원하지 않는 소셜로그인입니다."),
	/**
	 * 인증에 필수적인 정보가 OAuth provider 로부터 전달되지 않았음. 사용자가 개인정보 제공에 비동의했거나, 제대로 계정 정보를 설정하지 않은 경우 발생
	 */
	INVALID_OAUTH2_INFO(401, "A003", "잘못된 OAuth 정보입니다."),

	/**
	 * request 에 담긴 토큰정보를 사용해 기존 사용자 정보를 조회하였으나, 존재하지 않는 경우 발생. 또는 최초 소셜로그인 시도하였으나 가입한 회원이 아니라면 해당 오류
	 * 발생
	 *
	 */
	JWT_INVALID(401, "A005", "유효하지 않은 토큰입니다."),
	JWT_EXPIRED(401, "A006", "만료된 토큰입니다."),
	JWT_MISSING(401, "A007", "토큰이 존재하지 않습니다."),
	EXPIRED_REFRESH_TOKEN(401, "A008", "만료된 REFRESH 토큰입니다. 재로그인 해주십시오.");

	private final int status;
	private final String code;
	private final String message;
}
