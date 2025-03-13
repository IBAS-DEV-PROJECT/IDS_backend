package com.example.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spoti.api.auth.domain.oauth2.socialShare.dto.ShareTrackingRequest;
import com.spoti.api.auth.domain.oauth2.socialShare.dto.SocialShareRequest;
import com.spoti.api.auth.domain.oauth2.socialShare.dto.SocialShareResponse;
import com.spoti.api.auth.domain.oauth2.socialShare.usecase.KakaoSocialShareServiceImpl;
import com.spoti.api.auth.domain.oauth2.socialShare.usecase.SocialShareServiceImpl;
import com.spoti.api.auth.domain.oauth2.socialShare.usecase.XSocialShareServiceImpl;
import com.spoti.api.auth.domain.oauth2.socialShare.usecase.InstagramSocialShareServiceImpl;
import com.spoti.api.auth.domain.error.ErrorResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * 소셜 미디어 공유 기능을 위한 통합 컨트롤러
 */
@RestController
@RequestMapping("/api/share")
@Tag(name = "소셜 미디어 공유", description = "소셜 미디어 공유 관련 API")
public class SocialShareController {

	private final SocialShareServiceImpl socialShareService;
	private final KakaoSocialShareServiceImpl kakaoShareService;
	private final XSocialShareServiceImpl xShareService;
	private final InstagramSocialShareServiceImpl instagramShareService;

	public SocialShareController(
		SocialShareServiceImpl socialShareService,
		KakaoSocialShareServiceImpl kakaoShareService,
		XSocialShareServiceImpl xShareService,
		InstagramSocialShareServiceImpl instagramShareService) {
		this.socialShareService = socialShareService;
		this.kakaoShareService = kakaoShareService;
		this.xShareService = xShareService;
		this.instagramShareService = instagramShareService;
	}

	/**
	 * 소셜 미디어 공유 URL 생성 통합 API
	 */
	@Operation(summary = "공유 URL 생성", description = "특정 플랫폼에 대한 소셜 미디어 공유 URL을 생성합니다")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "공유 URL 생성 성공",
			content = @Content(
				schema = @Schema(implementation = SocialShareResponse.class),
				examples = @ExampleObject(
					value = "{\"success\": true, \"message\": \"공유 URL 생성 성공\", \"shareUrl\": \"https://example.com/share/123\"}"
				)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "잘못된 요청",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = @ExampleObject(
					value = "{\"status\": 400, \"code\": \"S001\", \"message\": \"지원하지 않는 플랫폼입니다.\"}"
				)
			)
		),
		@ApiResponse(
			responseCode = "500",
			description = "서버 오류",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = @ExampleObject(
					value = "{\"status\": 500, \"code\": \"S002\", \"message\": \"공유 URL 생성 오류\"}"
				)
			)
		)
	})

	@GetMapping("/url")
	public ResponseEntity<SocialShareResponse> getShareUrl(
		@Parameter(description = "공유할 컨텐츠 ID", required = true) @RequestParam Long contentId,
		@Parameter(description = "공유 플랫폼 (kakao, x, instagram)", required = true) @RequestParam String platform,
		@Parameter(description = "사용자 정의 공유 메시지") @RequestParam(required = false) String message) {

		try {
			SocialShareResponse response;

			switch (platform.toLowerCase()) {
				case "kakao":
					// 카카오톡 전용 템플릿 호출 메서드 사용
					response = kakaoShareService.getKakaoTemplateByContentId(contentId);
					// API 키 추가 (필요한 경우)
					response.addPlatformData("kakaoApiKey", kakaoShareService.getKakaoApiKey());
					break;

				case "x":
					response = xShareService.generateShareInfo(contentId, message);
					// X 특정 URL 설정
					String xShareUrl = xShareService.generateXShareUrl(contentId, message);
					response.setShareUrl(xShareUrl);
					break;

				case "instagram":
					response = instagramShareService.generateShareInfo(contentId, message);
					break;

				default:
					return ResponseEntity.badRequest().body(
						SocialShareResponse.failure("지원하지 않는 플랫폼입니다: " + platform));
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(
				SocialShareResponse.failure("공유 URL 생성 오류: " + e.getMessage()));
		}
	}

	/**
	 * 모든 소셜 미디어 공유 정보 통합 API
	 */
	@Operation(summary = "모든 플랫폼 공유 정보 조회", description = "모든 소셜 미디어 플랫폼에 대한 공유 정보를 한 번에 조회합니다")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "공유 정보 조회 성공"
		),
		@ApiResponse(
			responseCode = "500",
			description = "서버 오류",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = @ExampleObject(
					value = "{\"status\": 500, \"code\": \"S003\", \"message\": \"공유 정보 생성 오류\"}"
				)
			)
		)
	})

	@GetMapping("/info")
	public ResponseEntity<Map<String, Object>> getShareInfo(
		@Parameter(description = "공유할 컨텐츠 ID", required = true) @RequestParam Long contentId,
		@Parameter(description = "사용자 정의 공유 메시지") @RequestParam(required = false) String message) {

		try {
			// 각 플랫폼별 공유 정보 생성
			SocialShareResponse kakaoResponse = kakaoShareService.generateShareInfo(contentId, message);
			SocialShareResponse xResponse = xShareService.generateShareInfo(contentId, message);
			SocialShareResponse instaResponse = instagramShareService.generateShareInfo(contentId, message);

			// 통합 정보 생성
			Map<String, Object> result = new HashMap<>();
			result.put("kakao", kakaoResponse);
			result.put("x", xResponse);
			result.put("instagram", instaResponse);

			return ResponseEntity.ok(result);

		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "공유 정보 생성 오류: " + e.getMessage());

			return ResponseEntity.internalServerError().body(error);
		}
	}

	/**
	 * 공유 통계 기록 API
	 */
	@Operation(summary = "공유 통계 기록", description = "소셜 미디어 공유 통계를 기록합니다")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "통계 기록 성공"
		),
		@ApiResponse(
			responseCode = "400",
			description = "잘못된 요청",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = @ExampleObject(
					value = "{\"status\": 400, \"code\": \"S004\", \"message\": \"유효하지 않은 통계 기록 요청\"}"
				)
			)
		)
	})

	@PostMapping("/track")
	public ResponseEntity<Void> trackShare(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "공유 통계 기록 정보",
			required = true,
			content = @Content(
				schema = @Schema(implementation = ShareTrackingRequest.class),
				examples = @ExampleObject(
					value = "{\"contentId\": 123, \"platform\": \"kakao\", \"userId\": 456}"
				)
			)
		)
		@RequestBody ShareTrackingRequest request) {

		String platform = request.getPlatform().toLowerCase();

		switch (platform) {
			case "kakao":
				kakaoShareService.trackShare(request.getContentId(), platform, request.getUserId());
				break;
			case "x":
				xShareService.trackShare(request.getContentId(), platform, request.getUserId());
				break;
			case "instagram":
				instagramShareService.trackShare(request.getContentId(), platform, request.getUserId());
				break;
			default:
				// 기본적으로 일반 서비스 사용
				socialShareService.trackShare(request.getContentId(), platform, request.getUserId());
				break;
		}

		return ResponseEntity.ok().build();
	}

	@PostMapping("/post")
	public ResponseEntity<SocialShareResponse> postToSocialMedia(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "소셜 미디어 공유 요청 정보",
			required = true,
			content = @Content(
				schema = @Schema(implementation = SocialShareRequest.class),
				examples = @ExampleObject(
					value = "{\"contentId\": 123, \"message\": \"공유 메시지\", \"platform\": \"x\", \"userId\": 456}"
				)
			)
		)
		@RequestBody SocialShareRequest request) {

		String platform = request.getPlatform().toLowerCase();

		try {
			SocialShareResponse response;

			switch (platform) {
				case "x":
					response = xShareService.shareToSocialMedia(
						request.getContentId(),
						request.getMessage());
					break;

				case "kakao":
					response = kakaoShareService.shareToSocialMedia(
						request.getContentId(),
						request.getMessage());
					break;

				case "instagram":
					response = instagramShareService.shareToSocialMedia(
						request.getContentId(),
						request.getMessage());
					break;

				default:
					return ResponseEntity.badRequest()
						.body(SocialShareResponse.failure("지원하지 않는 플랫폼입니다: " + platform));
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.internalServerError()
				.body(SocialShareResponse.failure("공유 처리 중 오류 발생: " + e.getMessage()));
		}
	}

}
