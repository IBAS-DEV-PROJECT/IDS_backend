package com.spoti.api.auth.domain.token.jwtUtils;

import com.spoti.api.auth.domain.token.TokenResolver;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtTokenResolver implements TokenResolver {
	private static final String AUTHORIZATION_HEADER = "Authorization";//JWT 토큰을 서버로 보낼 때 사용하는 HTTP 헤더
	private static final String AUTHORIZATION_TYPE = "Bearer "; //Bearer 로 시작

	@Override
	public String resolveAccessTokenOrNull(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER); //헤더에서 JWT 토큰 가져옴

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTHORIZATION_TYPE)) //Bearer 인지 확인
			return bearerToken.substring(7); //bearer 제외(7자) 토큰만 반환
		else return null;
	}
}
