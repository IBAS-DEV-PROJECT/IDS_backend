//HTTP 요청에서 액세스 토큰을 추출하는 인터페이스
package com.spoti.api.auth.domain.token;

//이를 통해 요청의 헤더(header), 파라미터(parameter), 바디(body) 등을 읽을 수 있음
import jakarta.servlet.http.HttpServletRequest;

public interface TokenResolver {

	/**
	 * @param request HttpServletRequest
	 * @return a resolved token from request header, otherwise null
	 */
	String resolveAccessTokenOrNull(HttpServletRequest request);
}
