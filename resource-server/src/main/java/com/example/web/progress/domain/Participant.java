package com.example.web.progress.domain;

import jakarta.persistence.*;

@Entity
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String participantName;

    @ManyToOne
    @JoinColumn(name = "test_progress_id")
    private TestProgress testProgress;

    // 기본 생성자
    public Participant() {}

    // 생성자
    public Participant(String participantName, TestProgress testProgress) {
        this.participantName = participantName;
        this.testProgress = testProgress;
    }

    // Getter 및 Setter
    public Long getId() {
        return id;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public TestProgress getTestProgress() {
        return testProgress;
    }

    public void setTestProgress(TestProgress testProgress) {
        this.testProgress = testProgress;
    }
}
