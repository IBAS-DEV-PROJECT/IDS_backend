package com.spoti.api.auth.domain.token.controller;

import com.spoti.api.auth.domain.token.RefreshRequestDto;
import com.spoti.api.auth.domain.token.TokenDto;

import com.spoti.api.auth.domain.token.TokenReIssuer;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequiredArgsConstructor
public class JwtTokenController {

  private final TokenReIssuer tokenReIssuer;

	@PostMapping("/token/refresh")
	@Operation(
		summary = "access token 재발급을 요청한다.",
		description = "Request Body에 refreshToken을 JSON 형식으로 넣어서 요청해야 합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "성공적으로 accessToken 재발급 완료"),
		@ApiResponse(responseCode = "401", description = "유효하지 않은 refreshToken")
	})
	public ResponseEntity<TokenDto> reissueAccessToken(@RequestBody RefreshRequestDto refreshRequestDto) {
		// 올바른 방식으로 refreshToken을 가져오기
		TokenDto newAccessToken = tokenReIssuer.reissueAccessToken(refreshRequestDto.getRefreshToken());

		return ResponseEntity.ok(newAccessToken);
	}

}
