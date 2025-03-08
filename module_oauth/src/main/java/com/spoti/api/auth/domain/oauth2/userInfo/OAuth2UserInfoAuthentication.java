package com.spoti.api.auth.domain.oauth2.userInfo;

import com.spoti.api.auth.domain.token.TokenAuthenticationResult;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class OAuth2UserInfoAuthentication extends TokenAuthenticationResult {

	private final String uid;

	private final String provider;

	private final String email;

	public OAuth2UserInfoAuthentication(String uid, String provider, String email) {
		super(null);
		this.uid = uid;
		this.provider = provider;
		this.email = email;
	}

	public OAuth2UserInfoAuthentication(
		String uid,
		String provider,
		String email,
		Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.uid = uid;
		this.provider = provider;
		this.email = email;
	}

	@Deprecated
	@Override
	public Object getCredentials() { return null; }

}
