package com.example.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/questions")  // 질문 관련 API 경로
public class QnAController {

    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private AnswerRepository answerRepository;

    // 랜덤 질문 제공 API
    @Operation(
        summary = "랜덤 질문 가져오기",
        description = "데이터베이스에서 무작위로 선택된 질문을 제공합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공적으로 랜덤 질문 반환",
            content = @Content(schema = @Schema(implementation = Question.class)))),
        @ApiResponse(responseCode = "404", description = "질문 데이터 없음",
            content = @Content(schema = @Schema(example = "{\"status\": 404, \"message\": \"질문 데이터가 없습니다.\"}"))))
    })
    @GetMapping("/")  // /questions 경로로 수정
    public ResponseEntity<Object> getRandomQuestion() {
        List<Question> questions = questionRepository.findAll();
        if (questions.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                "status", 404,
                "message", "질문 데이터가 없습니다."
            ));
        }
        Question randomQuestion = questions.get(new Random().nextInt(questions.size()));
        return ResponseEntity.ok(randomQuestion);
    }

    // 답변 저장 API
    @Operation(
        summary = "답변 저장하기",
        description = "사용자가 작성한 답변을 저장합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "답변 저장 완료",
            content = @Content(schema = @Schema(implementation = Answer.class)))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(example = "{\"status\": 400, \"message\": \"잘못된 요청입니다.\"}"))))
    })
    @PostMapping("/responses")  // /responses 경로로 수정
    public ResponseEntity<Object> saveAnswer(@RequestBody Answer answer) {
        if (answer == null || answer.getContent() == null || answer.getContent().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", 400,
                "message", "잘못된 요청입니다. 답변 내용을 입력해주세요."
            ));
        }
        Answer savedAnswer = answerRepository.save(answer);
        return ResponseEntity.ok(savedAnswer);
    }
}
