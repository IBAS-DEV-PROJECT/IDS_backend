package com.spoti.api.auth.domain.oauth2.socialShare.usecase;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.spoti.api.auth.domain.oauth2.socialShare.dto.SocialShareResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.spoti.api.auth.domain.oauth2.socialShare.domain.Content;
import com.spoti.api.auth.domain.oauth2.socialShare.domain.ContentNotFoundException;
import com.spoti.api.auth.domain.oauth2.socialShare.repository.ContentRepository;
import com.spoti.api.auth.domain.oauth2.socialShare.repository.ShareStatisticsRepository;

/**
 * 인스타그램 공유 기능 구현 클래스
 */
@Service
public class InstagramSocialShareServiceImpl extends SocialShareServiceImpl {

	@Value("${app.base-url}")
	private String baseUrl;

	public InstagramSocialShareServiceImpl(
		ContentRepository contentRepository,
		ShareStatisticsRepository shareStatisticsRepository) {
		super(contentRepository, shareStatisticsRepository);
	}

	@Override
	protected void addPlatformSpecificInfo(Map<String, Object> result, Content content, String baseUrl) {
		// 인스타그램 공유 URL 생성
		String shareUrl = createShareUrlWithUtm(baseUrl, "instagram");

		try {
			// 1. 인스타그램 스토리 딥링크 생성
			String storyDeepLink = generateInstagramStoryDeepLink(
				content.getThumbnailUrl(),
				shareUrl,
				content.getTitle());

			result.put("instagramStoryDeepLink", storyDeepLink);
			result.put("instagramShareUrl", shareUrl);
			result.put("instagramCopyMessage", content.getTitle() + " " + shareUrl);
			result.put("instagramShareInstructions",
				"인스타그램 공유 방법:\n" +
					"1. 스토리 공유: '인스타그램 스토리에 공유' 버튼을 클릭하세요.\n" +
					"2. 포스트 공유: 링크를 복사하여 포스트에 붙여넣으세요.\n" +
					"3. DM 공유: 링크를 복사하여 DM으로 공유하세요.");
		} catch (UnsupportedEncodingException e) {
			result.put("error", "인스타그램 딥링크 생성 오류: " + e.getMessage());
		}
	}

	/**
	 * 인스타그램 스토리 딥링크 생성
	 */
	public String generateInstagramStoryDeepLink(String backgroundImageUrl, String linkUrl, String title)
		throws UnsupportedEncodingException {
		String encodedImageUrl = URLEncoder.encode(backgroundImageUrl, StandardCharsets.UTF_8.toString());
		String encodedLinkUrl = URLEncoder.encode(linkUrl, StandardCharsets.UTF_8.toString());

		return "instagram-stories://share?" +
			"background_image=" + encodedImageUrl +
			"&source_url=" + encodedLinkUrl;
	}

	/**
	 * 인스타그램 공유 정보 생성 (스토리 및 URL 공유)
	 */
	public SocialShareResponse getInstagramShareInfo(Long contentId) {
		Content content = contentRepository.findById(contentId)
			.orElseThrow(() -> new ContentNotFoundException("컨텐츠를 찾을 수 없습니다: " + contentId));

		String shareUrl = baseUrl + "/view/" + contentId + "?utm_source=instagram&utm_medium=share&utm_campaign=share";
		SocialShareResponse response = SocialShareResponse.success(shareUrl);

		try {
			String storyDeepLink = generateInstagramStoryDeepLink(
				content.getThumbnailUrl(),
				shareUrl,
				content.getTitle());

			response.addPlatformData("storyDeepLink", storyDeepLink);
			response.addPlatformData("imageUrl", content.getThumbnailUrl());
			response.addPlatformData("title", content.getTitle());
			response.addPlatformData("copyText", content.getTitle() + " " + shareUrl);

			String encodedUrl = URLEncoder.encode(shareUrl, StandardCharsets.UTF_8.toString());
			String encodedText = URLEncoder.encode(content.getTitle(), StandardCharsets.UTF_8.toString());
			response.addPlatformData("dmShareUrl", "instagram://sharesheet?text=" + encodedText + "%20" + encodedUrl);

			response.setMessage("인스타그램 공유 정보 생성 성공");
		} catch (UnsupportedEncodingException e) {
			return SocialShareResponse.failure("인스타그램 공유 정보 생성 오류: " + e.getMessage());
		}

		return response;
	}

	/**
	 * 인스타그램 URL 공유 정보만 반환
	 */
	public SocialShareResponse generateInstagramShareUrl(Long contentId, String customText) {
		Content content = contentRepository.findById(contentId)
			.orElseThrow(() -> new ContentNotFoundException("컨텐츠를 찾을 수 없습니다: " + contentId));

		String shareUrl = baseUrl + "/view/" + contentId + "?utm_source=instagram&utm_medium=share&utm_campaign=share";
		SocialShareResponse response = SocialShareResponse.success(shareUrl);

		String shareText = (customText != null && !customText.isEmpty()) ? customText : content.getTitle();
		response.setCopyText(shareText + " " + shareUrl);

		return response;
	}

	/**
	 * 인스타그램에 직접 공유하기 (현재 API 미지원)
	 */
	@Override
	public SocialShareResponse shareToSocialMedia(Long contentId, String message, String authToken) {
		return SocialShareResponse.failure("인스타그램은 API를 통한 직접 공유를 지원하지 않습니다.");
	}
}
