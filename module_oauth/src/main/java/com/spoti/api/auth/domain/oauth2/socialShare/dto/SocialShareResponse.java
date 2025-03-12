package com.spoti.api.auth.domain.oauth2.socialShare.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.HashMap;
import java.util.Map;

/**
 * 소셜 미디어 공유 응답 DTO
 */
@Setter
@Getter
public class SocialShareResponse {

	private boolean success;
	private String message;
	private String shareUrl;
	private String copyText;
	private Map<String, Object> platformSpecificData; // 플랫폼별 추가 데이터

	// 기본 생성자
	public SocialShareResponse() {
		this.success = true;
		this.platformSpecificData = new HashMap<>();
	}

	// 성공 응답 생성 팩토리 메소드
	public static SocialShareResponse success(String shareUrl) {
		SocialShareResponse response = new SocialShareResponse();
		response.setShareUrl(shareUrl);
		return response;
	}

	// 실패 응답 생성 팩토리 메소드
	public static SocialShareResponse failure(String errorMessage) {
		SocialShareResponse response = new SocialShareResponse();
		response.setSuccess(false);
		response.setMessage(errorMessage);
		return response;
	}

	// 플랫폼별 데이터 추가 메소드
	public void addPlatformData(String key, Object value) {
		this.platformSpecificData.put(key, value);
	}
}
