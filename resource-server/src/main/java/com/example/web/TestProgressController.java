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
@RequestMapping("/api")
public class QnAController {
    
    @Autowired
    private TestProgressRepository testProgressRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    // 현재 테스트 진행 상황 조회 API
    @Operation(
        summary = "현재 테스트 진행 상황 조회",
        description = "사용자가 테스트를 어디까지 진행했는지 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "진행 상황 반환",
            content = @Content(schema = @Schema(implementation = TestProgress.class))),
        @ApiResponse(responseCode = "404", description = "진행 상황 없음",
            content = @Content(schema = @Schema(example = "{\"status\": 404, \"message\": \"진행 상황을 찾을 수 없습니다.\"}")))
    })
    @GetMapping("/test-progress/{userId}")
    public ResponseEntity<Object> getTestProgress(@PathVariable Long userId) {
        Optional<TestProgress> progress = testProgressRepository.findByUserId(userId);
        return progress.map(ResponseEntity::ok).orElseGet(() -> 
            ResponseEntity.status(404).body(Map.of(
                "status", 404,
                "message", "진행 상황을 찾을 수 없습니다."
            ))
        );
    }

    // 현재 테스트 진행 상황 저장 API
    @Operation(
        summary = "현재 테스트 진행 상황 저장",
        description = "사용자의 테스트 진행 상황을 저장합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "진행 상황 저장 완료",
            content = @Content(schema = @Schema(implementation = TestProgress.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(example = "{\"status\": 400, \"message\": \"잘못된 요청입니다.\"}")))
    })
    @PostMapping("/test-progress")
    public ResponseEntity<Object> saveTestProgress(@RequestBody TestProgress testProgress) {
        if (testProgress == null || testProgress.getUserId() == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", 400,
                "message", "잘못된 요청입니다. 사용자 ID가 필요합니다."
            ));
        }
        TestProgress savedProgress = testProgressRepository.save(testProgress);
        return ResponseEntity.ok(savedProgress);
    }

    // 참여 인원 카운팅 API
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
