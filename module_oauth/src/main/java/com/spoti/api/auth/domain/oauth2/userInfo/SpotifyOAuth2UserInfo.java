package com.spoti.api.auth.domain.oauth2.userInfo;

import com.spoti.api.auth.domain.oauth2.OAuth2Provider;

import java.util.Map;
import java.util.Objects;

public class SpotifyOAuth2UserInfo extends OAuth2UserInfo {

	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "nickname";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_IMAGE_URL = "profile_image_url";
	private static final String KEY_EXTRA_DATA = "spotify_account";

	public SpotifyOAuth2UserInfo(Map<String, Object> attributes) {
		super(OAuth2Provider.SPOTIFY, attributes);
	}

	@Override
	public String getId() {
		return Objects.toString(attributes.get(KEY_ID), ""); //안전한 null 처리 null 이면 "" 반환
	}

	@Override
	public String getName() {
		return Objects.toString(attributes.get(KEY_NAME), "");
	}

	@Override
	public String getEmail() {
		return Objects.toString(attributes.get(KEY_EMAIL), "");
	}

	@Override
	public String getImageUrl() {
		return Objects.toString(attributes.get(KEY_IMAGE_URL), "");
	}

	@SuppressWarnings({"unchecked"})
	@Override
	public Map<String, Object> getExtraData() {

		return (Map<String, Object>) attributes.get(KEY_EXTRA_DATA);
	}
}
