package com.spoti.api.auth.domain.oauth2.handler;

import com.spoti.api.auth.config.AuthProperties;
import com.spoti.api.auth.domain.error.authException.UnauthorizedRedirectUrlException;
import com.spoti.api.auth.domain.oauth2.cookie.CookieUtils;
import com.spoti.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import com.spoti.api.auth.domain.token.TokenUtil;
import com.spoti.api.auth.domain.oauth2.userInfo.OAuth2UserInfoFactory;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static com.spoti.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URL_PARAM_COOKIE_NAME;

@RequiredArgsConstructor
@Slf4j
public class Oauth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final TokenUtil tokenUtil;
	private final AuthProperties authProperties;
	private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request, HttpServletResponse response, Authentication authentication)
		throws IOException {
		String tagetUrl = this.determineTargetUrl(request, response, authentication);

		if(response.isCommitted()) {
			log.debug("Response has already been committed");
			return;
		}

		this.clearAuthenticationAttributes(request);
		this.httpCookieOAuth2AuthorizationRequestRepository.clearCookies(request, response);
		this.getRedirectStrategy().sendRedirect(request, response, tagetUrl);
	}

	/**
	 * @param authentication 인증 완료된 결과
	 * @return 인증 결과를 사용해서 access 토큰을 발급하고, 쿠키에 저장되어 있던 redirect_uri(프론트에서 적어준 것)와 합쳐서 반환. 명시되지 않으면
	 *     설정파일({@link AuthProperties})에 명시된 default redirect url 값 적용
	 */
	@Override
	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

		String targetUrl =
			CookieUtils.resolveCookie(request, REDIRECT_URL_PARAM_COOKIE_NAME)
				.map(Cookie::getValue)
				.orElse(authProperties.getOauth2().getDefaultRedirectUri());
		if(notAuthorized(targetUrl)) {
			throw new UnauthorizedRedirectUrlException();
		}

		String imageUrl =
			OAuth2UserInfoFactory.getOAuthUserInfo((OAuth2AuthenticationToken) authentication)
				.getImageUrl();

		return UriComponentsBuilder.fromUriString(targetUrl)
			.queryParam("accessToken", tokenUtil.createAccessToken(authentication))
			.queryParam("refreshToken", tokenUtil.createRefreshToken(authentication))
			.queryParam("expiresIn", tokenUtil.getExpiration())
			.queryParam("imageUrl", imageUrl)
			.build()
			.toUriString();
	}

	private boolean notAuthorized(String redirectUrl) {
		return !redirectUrl.isBlank()
			&& !authProperties.getOauth2().isAuthorizedRedirectUri(redirectUrl);
	}
}
