package com.spoti.api.auth.domain.oauth2;

import java.util.Arrays;

import com.spoti.api.auth.domain.error.authException.UnsupportedOAuth2ProviderException;

public enum OAuth2Provider {
	SPOTIFY;

	public static OAuth2Provider convert(String registrationId) {
		return Arrays.stream(OAuth2Provider.values())
			.filter(provider -> provider.toString().equals(registrationId.toUpperCase()))
			.findAny()
			.orElseThrow(UnsupportedOAuth2ProviderException::new);
	}
}
