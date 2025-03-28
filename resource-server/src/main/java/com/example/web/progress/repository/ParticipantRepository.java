package com.example.web.progress.repository;

import com.example.web.progress.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    // 추가적인 쿼리 메서드가 필요하면 여기에 정의합니다.
}
