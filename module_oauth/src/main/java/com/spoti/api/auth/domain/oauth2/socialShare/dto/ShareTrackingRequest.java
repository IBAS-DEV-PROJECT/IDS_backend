package com.spoti.api.auth.domain.oauth2.socialShare.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 공유 통계 기록 요청 DTO
 */
@Setter
@Getter
public class ShareTrackingRequest {

	private Long contentId;
	private String platform;
	private Long userId;

}
