package com.example.web.qna.dto;

public class AnswerDTO {
    private Long id;
    private String content;
    private Long questionId;

    public AnswerDTO(Long id, String content, Long questionId) {
        this.id = id;
        this.content = content;
        this.questionId = questionId;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Long getQuestionId() {
        return questionId;
    }
}
