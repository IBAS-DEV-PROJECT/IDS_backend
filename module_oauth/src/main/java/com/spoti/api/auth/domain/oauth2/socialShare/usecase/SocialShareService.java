package com.spoti.api.auth.domain.oauth2.socialShare.usecase;

import java.util.Map;
import com.spoti.api.auth.domain.oauth2.socialShare.dto.SocialShareResponse;

/**
 * 소셜 미디어 공유 기능을 위한 공통 인터페이스
 */
public interface SocialShareService {

	/**
	 * 컨텐츠 ID를 기반으로 공유 정보 생성
	 * @param contentId 컨텐츠 ID
	 * @param customMessage 사용자 지정 메시지 (선택사항)
	 * @return 공유 정보가 담긴 Map
	 */
	SocialShareResponse generateShareInfo(Long contentId, String customMessage);

	/**
	 * 공유 통계 기록
	 * @param contentId 공유된 컨텐츠 ID
	 * @param platform 공유 플랫폼 (예: "kakao", "x", "instagram")
	 * @param userId 사용자 ID (선택사항)
	 */
	void trackShare(Long contentId, String platform, Long userId);

	/**
	 * 사용자 인증 토큰 검증
	 * @param authToken 인증 토큰
	 * @return 유효성 여부
	 */
	boolean validateToken(String authToken);

	/**
	 * 소셜 미디어에 직접 콘텐츠 공유하기
	 * @param contentId 컨텐츠 ID
	 * @param message 공유 메시지
	 * @param authToken 인증 토큰
	 * @return 공유 결과 정보
	 */
	SocialShareResponse shareToSocialMedia(Long contentId, String message, String authToken);
}
