package com.spoti.api.auth.domain.oauth2.handler;

import com.spoti.api.auth.config.AuthProperties;
import com.spoti.api.auth.domain.oauth2.cookie.CookieUtils;
import com.spoti.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import com.spoti.api.auth.domain.token.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

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
}
