package com.example.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/test-progress")  // 진행 상황 관련 API 경로
public class TestProgressController {

    @Autowired
    private TestProgressRepository testProgressRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Operation(
        summary = "참여 인원 카운팅",
        description = "이 테스트에 총 몇 명이 참여했는지 반환합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "참여 인원 수 반환",
            content = @Content(schema = @Schema(type = "integer", format = "int64")))
    })
    @GetMapping("/participants/count")
    public ResponseEntity<Map<String, Object>> getParticipantCount() {
        long count = participantRepository.count();
        return ResponseEntity.ok(Map.of(
            "status", 200,
            "participantCount", count
        ));
    }
}
