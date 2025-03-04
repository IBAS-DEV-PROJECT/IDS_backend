package com.spoti.api.auth.config;

import com.spoti.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import com.spoti.api.auth.domain.oauth2.handler.Oauth2AuthenticationFailureHandler;
import com.spoti.api.auth.domain.oauth2.handler.Oauth2AuthenticationSuccessHandler;
import com.spoti.api.auth.domain.token.jwtUtils.JwtTokenUtil;
import com.spoti.api.auth.domain.token.jwtUtils.refreshToken.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AuthProperties.class)  // AuthProperties를 Spring Bean으로 등록
public class AuthBeansConfig {

	private final JwtTokenUtil jwtTokenUtil;
	private final AuthProperties authProperties;
	private final RefreshTokenRepository refreshTokenRepository;

	// OAuth2 Authorization 요청을 쿠키에 저장하기 위한 Bean
	@Bean
	public HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository() {
		return new HttpCookieOAuth2AuthorizationRequestRepository();
	}

	// OAuth2 인증 성공 핸들러
	@Bean
	public Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler() {
		return new Oauth2AuthenticationSuccessHandler(
			jwtTokenUtil, this.authProperties, this.httpCookieOAuth2AuthorizationRequestRepository());
	}

	// OAuth2 인증 실패 핸들러 추가
	@Bean
	public Oauth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler() {
		return new Oauth2AuthenticationFailureHandler(
			this.authProperties, this.httpCookieOAuth2AuthorizationRequestRepository());
	}

	// OAuth2 클라이언트 서비스 (DB 저장 방식)
	@Bean
	public OAuth2AuthorizedClientService authorizedClientService(
		DataSource dataSource, ClientRegistrationRepository clientRegistrationRepository) {
		return new JdbcOAuth2AuthorizedClientService(
			new JdbcTemplate(dataSource), clientRegistrationRepository);
	}
}
