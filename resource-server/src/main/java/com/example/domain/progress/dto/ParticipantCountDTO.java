package com.example.domain.progress.dto;

public class ParticipantCountDTO {

    private int participantCount;

    // 기본 생성자
    public ParticipantCountDTO() {}

    // 생성자
    public ParticipantCountDTO(int participantCount) {
        this.participantCount = participantCount;
    }

    // Getter 및 Setter
    public int getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }
}
