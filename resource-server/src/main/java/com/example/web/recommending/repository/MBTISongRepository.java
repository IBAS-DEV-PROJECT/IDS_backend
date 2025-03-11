package com.example.web.recommending.repository;

import com.example.web.recommending.domain.MBTISong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MBTISongRepository extends JpaRepository<MBTISong, Integer> {
	//SQL 사용하지 않고 수정
	@Query(value = "SELECT * FROM mbti_songs WHERE mbti_type = :mbti ORDER BY RAND() LIMIT 10", nativeQuery = true)
	List<MBTISong> findRandomSongsByMBTI(@Param("mbti") String mbti);
}
