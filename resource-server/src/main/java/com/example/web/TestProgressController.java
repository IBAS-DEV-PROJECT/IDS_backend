package com.example.web;

import com.example.domain.progress.dto.ParticipantCountDTO;
import com.example.domain.progress.usecase.TestProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test-progress")
public class TestProgressController {

    @Autowired
    private TestProgressService testProgressService;

    @Operation(
        summary = "참여 인원 카운팅",
        description = "이 테스트에 총 몇 명이 참여했는지 반환합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "참여 인원 수 반환",
            content = @Content(schema = @Schema(implementation = ParticipantCountDTO.class)))
    })
    @GetMapping("/participants/count")
    public ResponseEntity<ParticipantCountDTO> getParticipantCount() {
        long count = testProgressService.getParticipantCount();
        ParticipantCountDTO response = new ParticipantCountDTO((int) count);
        return ResponseEntity.ok(response);
    }
}
