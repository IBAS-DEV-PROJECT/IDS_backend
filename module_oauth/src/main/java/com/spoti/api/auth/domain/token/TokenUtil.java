package com.spoti.api.auth.domain.token;

import org.springframework.security.core.Authentication;
import io.jsonwebtoken.JwtException;

public interface TokenUtil {

	/**
	 * 주어진 JWT 토큰을 검증하는 메서드
	 *
	 * @param token JWT 토큰
	 * @throws JwtException 유효하지 않은 토큰일 경우 예외 발생
	 */
	void validate(String token) throws JwtException;

	/**
	 * JWT 토큰을 기반으로 인증(Authentication) 객체를 반환하는 메서드
	 *
	 * @param token JWT 토큰
	 * @return Authentication 객체
	 */
	Authentication getAuthentication(String token);

	/**
	 * 리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급하는 메서드
	 *
	 * @param refreshToken 리프레시 토큰
	 * @return 새로운 액세스 토큰을 포함한 TokenDto 객체
	 * @throws JwtException 유효하지 않은 토큰일 경우 예외 발생
	 */
	TokenDto reissueAccessTokenUsing(String refreshToken) throws JwtException;

	/**
	 * OAuth 2.0 인증 결과를 기반으로 액세스 토큰을 생성하는 메서드
	 *
	 * @param authentication OAuth 2.0 인증 객체
	 * @return 생성된 액세스 토큰이 포함된 TokenDto 객체
	 */
	//TokenDto createAccessToken(Authentication authentication);

	/**
	 * OAuth 2.0 인증 결과를 기반으로 리프레시 토큰을 생성하는 메서드
	 *
	 * @param authentication OAuth 2.0 인증 객체
	 * @return 생성된 리프레시 토큰이 포함된 TokenDto 객체
	 */
	//TokenDto createRefreshToken(Authentication authentication);

	/**
	 * 액세스 토큰의 유효 기간을 초 단위로 반환하는 메서드
	 *
	 * @return 액세스 토큰 만료 시간 (초 단위)
	 */
	long getExpiration();
}
