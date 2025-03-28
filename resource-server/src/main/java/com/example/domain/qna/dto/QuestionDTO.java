package com.example.domain.qna.dto;

public class QuestionDTO {
    private Long id;
    private String content;

    public QuestionDTO(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
