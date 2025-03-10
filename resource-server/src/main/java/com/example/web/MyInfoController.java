package com.example.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.argumentResolver.Authenticated;
import com.spoti.api.auth.domain.error.ErrorResponse;

@Slf4j
@RestController
@Tag(name = "회원정보", description = "회원 정보 확인")
@RequiredArgsConstructor
public class MyInfoController {

	@Operation(summary = "인증 상태 확인", description = "사용자의 로그인/인증 상태를 확인합니다")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "200",
				description = "인증된 사용자"
			),
			@ApiResponse(
				responseCode = "401",
				description = "인증되지 않은 사용자",
				content = @Content(
					schema = @Schema(implementation = ErrorResponse.class),
					examples = @ExampleObject(
						value = "{\"status\": 401, \"code\": \"A007\", \"message\": \"토큰이 존재하지 않습니다.\"}"
					)
				)
			)
		}
	)
	@GetMapping("/check")
	public ResponseEntity<Void> checkAuth(@Authenticated Long memberId) {
		return ResponseEntity.ok().build();
	}
}
