package com.spoti.api.auth.domain.oauth2.socialShare.usecase;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.spoti.api.auth.domain.oauth2.socialShare.domain.Content;
import com.spoti.api.auth.domain.oauth2.socialShare.domain.ContentNotFoundException;
import com.spoti.api.auth.domain.oauth2.socialShare.dto.SocialShareResponse;
import com.spoti.api.auth.domain.oauth2.socialShare.repository.ContentRepository;
import com.spoti.api.auth.domain.oauth2.socialShare.repository.ShareStatisticsRepository;

/**
 * 카카오톡 공유 기능 구현 클래스
 */
@Getter
@Service
@Primary
public class KakaoSocialShareServiceImpl extends SocialShareServiceImpl {

	@Value("${NEXT_PUBLIC_KAKAO_API_KEY}")
	protected String kakaoApiKey;

	public KakaoSocialShareServiceImpl(
		ContentRepository contentRepository,
		ShareStatisticsRepository shareStatisticsRepository) {
		super(contentRepository, shareStatisticsRepository);
	}

	@Override
	protected void addPlatformSpecificInfo(Map<String, Object> result, Content content, String baseUrl) {
		// 카카오톡 공유 URL 생성
		String kakaoShareUrl = createShareUrlWithUtm(baseUrl, "kakao");

		// 카카오톡에서만 사용되는 특정 정보 추가
		result.put("kakaoApiKey", kakaoApiKey);

		// 카카오톡 메시지 템플릿 정보 추가
		Map<String, Object> kakaoTemplate = generateKakaoTemplate(content, kakaoShareUrl);
		result.put("kakaoTemplate", kakaoTemplate);
	}

	/**
	 * 카카오톡 메시지 템플릿 정보 생성
	 */
	public Map<String, Object> generateKakaoTemplate(Content content, String shareUrl) {
		Map<String, Object> template = new HashMap<>();
		template.put("objectType", "feed");

		Map<String, Object> contentMap = new HashMap<>();
		contentMap.put("title", content.getTitle());
		contentMap.put("description", content.getDescription());
		contentMap.put("imageUrl", content.getThumbnailUrl());

		Map<String, String> link = new HashMap<>();
		link.put("webUrl", shareUrl);
		link.put("mobileWebUrl", shareUrl);
		contentMap.put("link", link);

		template.put("content", contentMap);

		List<Map<String, Object>> buttons = new ArrayList<>();
		Map<String, Object> button = new HashMap<>();
		button.put("title", "자세히 보기");

		Map<String, String> buttonLink = new HashMap<>();
		buttonLink.put("webUrl", shareUrl);
		buttonLink.put("mobileWebUrl", shareUrl);
		button.put("link", buttonLink);

		buttons.add(button);
		template.put("buttons", buttons);

		return template;
	}

	/**
	 * 컨텐츠 ID로 카카오톡 메시지 템플릿 정보 조회
	 */
	public SocialShareResponse getKakaoTemplateByContentId(Long contentId) {
		Content content = contentRepository.findById(contentId)
			.orElseThrow(() -> new ContentNotFoundException("컨텐츠를 찾을 수 없습니다: " + contentId));

		String shareUrl = baseUrl + "/view/" + contentId + "?utm_source=kakao&utm_medium=social&utm_campaign=share";
		SocialShareResponse response = SocialShareResponse.success(shareUrl);

		Map<String, Object> kakaoTemplate = generateKakaoTemplate(content, shareUrl);
		response.addPlatformData("kakaoTemplate", kakaoTemplate);
		response.setMessage("카카오톡 공유 정보 생성 성공");

		return response;
	}

	/**
	 * 카카오톡에 직접 공유하기 (현재 API 미지원)
	 */
	@Override
	public SocialShareResponse shareToSocialMedia(Long contentId, String message) {
		return SocialShareResponse.failure("카카오톡은 SDK를 통한 클라이언트 측 공유만 지원합니다.");
	}
}
