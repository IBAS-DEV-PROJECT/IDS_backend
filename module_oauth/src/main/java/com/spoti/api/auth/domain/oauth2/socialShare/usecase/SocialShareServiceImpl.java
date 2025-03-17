package com.spoti.api.auth.domain.oauth2.socialShare.usecase;

import com.spoti.api.auth.domain.oauth2.socialShare.domain.Content;
import com.spoti.api.auth.domain.oauth2.socialShare.domain.ContentNotFoundException;
import com.spoti.api.auth.domain.oauth2.socialShare.domain.ShareStatistics;
import com.spoti.api.auth.domain.oauth2.socialShare.dto.SocialShareResponse;
import com.spoti.api.auth.domain.oauth2.socialShare.repository.ContentRepository;
import com.spoti.api.auth.domain.oauth2.socialShare.repository.ShareStatisticsRepository;
import com.spoti.api.auth.domain.token.exception.InvalidTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class SocialShareServiceImpl implements SocialShareService {

	protected final ContentRepository contentRepository;
	protected final ShareStatisticsRepository shareStatisticsRepository;

	@Value("${app.base-url}")
	protected String baseUrl;

	public SocialShareServiceImpl(
		ContentRepository contentRepository,
		ShareStatisticsRepository shareStatisticsRepository) {
		this.contentRepository = contentRepository;
		this.shareStatisticsRepository = shareStatisticsRepository;
	}

	/**
	 * 컨텐츠 ID를 기반으로 공유 정보 생성
	 */
	@Override
	public SocialShareResponse generateShareInfo(Long contentId, String customMessage) {
		// 컨텐츠 정보 조회
		Content content = contentRepository.findById(contentId)
			.orElseThrow(() -> new ContentNotFoundException("컨텐츠를 찾을 수 없습니다: " + contentId));

		// 공유 URL 생성
		String shareUrl = baseUrl + "/view/" + contentId;

		// 응답 객체 생성
		SocialShareResponse response = SocialShareResponse.success(shareUrl);
		response.setMessage("공유 정보를 성공적으로 생성했습니다.");
		response.setCopyText(customMessage != null ? customMessage : content.getTitle());
		response.addPlatformData("title", content.getTitle());
		response.addPlatformData("description", content.getDescription());
		response.addPlatformData("imageUrl", content.getThumbnailUrl());

		// 플랫폼별 추가 정보 추가
		addPlatformSpecificInfo(response.getPlatformSpecificData(), content, shareUrl);

		return response;
	}

	/**
	 * 각 플랫폼별 특정 정보 추가 (기본 구현)
	 */
	protected void addPlatformSpecificInfo(Map<String, Object> result, Content content, String baseUrl) {
		// 기본적으로 아무것도 추가하지 않음 (필요하면 오버라이드 가능)
	}

	/**
	 * 공유 URL 생성 (UTM 파라미터 추가)
	 */
	protected String createShareUrlWithUtm(String baseUrl, String platform) {
		return baseUrl + "?utm_source=" + platform + "&utm_medium=social&utm_campaign=share";
	}

	/**
	 * 공유 통계 기록
	 */
	@Override
	public void trackShare(Long contentId, String platform, Long userId) {
		ShareStatistics stats = new ShareStatistics();
		stats.setContentId(contentId);
		stats.setPlatform(platform);
		stats.setUserId(userId);
		stats.setSharedAt(LocalDateTime.now());

		shareStatisticsRepository.save(stats);
	}

	/**
	 * 소셜 미디어에 직접 콘텐츠 공유하기 (기본 구현)
	 */
	@Override
	public SocialShareResponse shareToSocialMedia(Long contentId, String message) {
		return SocialShareResponse.failure("이 플랫폼은 직접 공유를 지원하지 않습니다.");
	}

	/**
	 * 인증 헤더에서 OAuth 토큰 추출
	 */
	protected String extractOAuthToken(String authHeader) {
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}
		throw new InvalidTokenException();
	}
}
