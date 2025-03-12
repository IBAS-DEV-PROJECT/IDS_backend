package com.spoti.api.auth.domain.oauth2.socialShare.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 소셜 미디어 공유 공통 요청 DTO
 */
@Setter
@Getter
public class SocialShareRequest {

	private Long contentId;
	private String message;
	private String platform; // "kakao", "x", "instagram" 등
	private Long userId;

	// 공통 필드 외에 플랫폼별 추가 정보를 위한 선택적 필드
	private Boolean withImage; // 이미지 포함 여부
	private String customImage; // 커스텀 이미지 URL
}
