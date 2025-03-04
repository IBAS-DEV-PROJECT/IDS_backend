package com.spoti.api.auth.domain.oauth2.handler;

import com.spoti.api.auth.config.AuthProperties;
import com.spoti.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class Oauth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private final AuthProperties authProperties;
	private final HttpCookieOAuth2AuthorizationRequestRepository
		httpCookieOAuth2AuthorizationRequestRepository;
	private static final String ERROR_PARAM = "?error=";

	@Override
	public void onAuthenticationFailure(
		HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
		throws IOException {

		String targetUrl = getAuthorizedTargetUrl(exception);

		httpCookieOAuth2AuthorizationRequestRepository.clearCookies(request, response);
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	private String getAuthorizedTargetUrl(AuthenticationException exception) {

		StringBuilder targetUrl = new StringBuilder();
		targetUrl.append(authProperties.getOauth2().getDefaultRedirectUri());

		String encodedErrorMessage =
			URLEncoder.encode(getExceptionMessage(exception), StandardCharsets.UTF_8);
		targetUrl.append(ERROR_PARAM).append(encodedErrorMessage);

		return targetUrl.toString();
	}

	private String getExceptionMessage(AuthenticationException exception) {

		if (exception instanceof OAuth2AuthenticationException) {
			return ((OAuth2AuthenticationException) exception).getError().getErrorCode();
		} else {
			return exception.getLocalizedMessage();
		}
	}
}
