//Spring Security의 Authentication 구현체,
//JWT를 Spring Security에서 사용할 수 있는 인증 객체로 변환
package com.spoti.api.auth.domain.token.jwtUtils;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private JwtAuthenticationToken(
		Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
	}

	private JwtAuthenticationToken(Object principal, Object credentials) {
		super(principal, credentials);
	}

	public static JwtAuthenticationToken of(String jwt) { return new JwtAuthenticationToken(jwt, jwt);}

	public static JwtAuthenticationToken of(
		Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
		return new JwtAuthenticationToken(principal, credentials, authorities);
	}
}
