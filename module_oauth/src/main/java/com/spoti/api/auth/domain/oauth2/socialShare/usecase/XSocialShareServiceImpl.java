package com.spoti.api.auth.domain.oauth2.socialShare.usecase;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.spoti.api.auth.domain.oauth2.socialShare.domain.Content;
import com.spoti.api.auth.domain.oauth2.socialShare.domain.ContentNotFoundException;
import com.spoti.api.auth.domain.oauth2.socialShare.dto.SocialShareResponse;
import com.spoti.api.auth.domain.oauth2.socialShare.repository.ContentRepository;
import com.spoti.api.auth.domain.oauth2.socialShare.repository.ShareStatisticsRepository;

/**
 * X(트위터) 공유 기능 구현 클래스 - URL 공유만 지원
 */
@Service
public class XSocialShareServiceImpl extends SocialShareServiceImpl {

	public XSocialShareServiceImpl(
		ContentRepository contentRepository,
		ShareStatisticsRepository shareStatisticsRepository) {
		super(contentRepository, shareStatisticsRepository);
	}

	@Override
	protected void addPlatformSpecificInfo(Map<String, Object> platformData, Content content, String shareUrl) {
		// X(트위터) 공유 URL 생성
		String xShareUrl = createShareUrlWithUtm(shareUrl, "x");

		try {
			// 공유 메시지 설정
			String shareMessage = content.getTitle();

			// URL 인코딩
			String encodedText = URLEncoder.encode(shareMessage, StandardCharsets.UTF_8.toString());
			String encodedUrl = URLEncoder.encode(xShareUrl, StandardCharsets.UTF_8.toString());

			// X(트위터) 공유 URL 생성
			String twitterIntentUrl = "https://x.com/intent/tweet?text=" + encodedText + "&url=" + encodedUrl;

			platformData.put("shareUrl", twitterIntentUrl);
		} catch (UnsupportedEncodingException e) {
			platformData.put("error", "URL 인코딩 오류: " + e.getMessage());
		}
	}

	/**
	 * X 공유 URL 생성
	 * 프론트엔드에서 window.open()으로 열 수 있는 URL을 생성
	 */
	public String generateXShareUrl(Long contentId, String customText) {
		try {
			Content content = contentRepository.findById(contentId)
				.orElseThrow(() -> new ContentNotFoundException("컨텐츠를 찾을 수 없습니다: " + contentId));

			// 공유할 URL 생성
			String contentUrl = baseUrl + "/view/" + contentId + "?utm_source=x&utm_medium=social&utm_campaign=share";

			// 공유 텍스트 생성
			String shareText = (customText != null && !customText.isEmpty())
				? customText
				: content.getTitle();

			// URL 인코딩
			String encodedText = URLEncoder.encode(shareText, StandardCharsets.UTF_8.toString());
			String encodedUrl = URLEncoder.encode(contentUrl, StandardCharsets.UTF_8.toString());

			// X 공유 URL 생성
			return "https://x.com/intent/tweet?text=" + encodedText + "&url=" + encodedUrl;

		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("URL 인코딩 오류: " + e.getMessage());
		}
	}

	/**
	 * X(트위터)로 직접 공유하기 (현재는 API 접근이 불가능하므로 지원하지 않음)
	 */
	@Override
	public SocialShareResponse shareToSocialMedia(Long contentId, String message, String authToken) {
		try {
			// 공유 URL 생성만 제공
			String shareUrl = generateXShareUrl(contentId, message);

			SocialShareResponse response = SocialShareResponse.failure("X API에 직접 접근할 수 없습니다. 대신 다음 URL을 사용하여 공유하세요.");
			response.addPlatformData("shareUrl", shareUrl);

			return response;
		} catch (Exception e) {
			return SocialShareResponse.failure("공유 URL 생성 오류: " + e.getMessage());
		}
	}
}
