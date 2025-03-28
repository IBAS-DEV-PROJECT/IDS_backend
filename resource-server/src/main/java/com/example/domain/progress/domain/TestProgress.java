package com.example.domain.progress.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class TestProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String testName;

    @OneToMany(mappedBy = "testProgress")
    private List<Participant> participants;

    // 기본 생성자
    public TestProgress() {}

    // 생성자
    public TestProgress(String testName) {
        this.testName = testName;
    }

    // Getter 및 Setter
    public Long getId() {
        return id;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }
}
