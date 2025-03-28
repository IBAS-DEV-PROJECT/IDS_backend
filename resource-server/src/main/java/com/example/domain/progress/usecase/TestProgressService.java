package com.example.domain.progress.usecase;

import com.example.domain.progress.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestProgressService {

    @Autowired
    private ParticipantRepository participantRepository;

    public long getParticipantCount() {
        return participantRepository.count();
    }
}
