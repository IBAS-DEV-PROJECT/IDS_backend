package com.spoti.api.auth.domain.oauth2.userInfo;

import com.spoti.api.auth.domain.error.authException.UnsupportedOAuth2ProviderException;
import com.spoti.api.auth.domain.oauth2.OAuth2Provider;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.util.Map;
import java.util.Objects;

public interface OAuth2UserInfoFactory {
	static OAuth2UserInfo getOAuthUserInfo(String registrationId, Map<String, Object> attributes) {
		OAuth2UserInfo userInfo = null;
		OAuth2Provider oAuth2Provider = OAuth2Provider.convert(registrationId);
		if (Objects.requireNonNull(oAuth2Provider) == OAuth2Provider.SPOTIFY) {
			userInfo = new SpotifyOAuth2UserInfo(attributes);
		} else {
			throw new UnsupportedOAuth2ProviderException();
		}
		return userInfo;
	}

	static OAuth2UserInfo getOAuthUserInfo(OAuth2AuthenticationToken auth2AuthenticationToken) {
		return OAuth2UserInfoFactory.getOAuthUserInfo(
			auth2AuthenticationToken.getAuthorizedClientRegistrationId(),
			auth2AuthenticationToken.getPrincipal().getAttributes());
	}
}
