package com.example.web;

import com.example.web.recommending.usecase.MBTISongService;
import com.example.web.recommending.dto.MBTISongResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/recommendation")
public class MBTIController {

	@Autowired
	private MBTISongService songService;

	@Operation(
		summary = "MBTI 유형별 노래 추천",
		description = "입력된 MBTI 유형을 기반으로 10개의 노래를 랜덤으로 추천합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "추천 노래 목록 반환",
			content = @Content(schema = @Schema(implementation = MBTISongResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청",
			content = @Content(schema = @Schema(example = "{\"status\": 400, \"message\": \"잘못된 요청입니다.\"}")))
	})
	@GetMapping
	public ResponseEntity<Object> getMBTISongs(
		@Parameter(description = "사용자의 MBTI 유형 (예: ISFP, ENTJ)", example = "ISTJ")
		@RequestParam(required = false) String mbti) {

		if (mbti == null || mbti.length() != 4) {
			return ResponseEntity.badRequest().body(Map.of(
				"status", 400,
				"message", "잘못된 요청입니다."
			));
		}

		MBTISongResponse response = songService.getRandomSongsByMBTI(mbti);
		return ResponseEntity.ok(response);
	}
}
