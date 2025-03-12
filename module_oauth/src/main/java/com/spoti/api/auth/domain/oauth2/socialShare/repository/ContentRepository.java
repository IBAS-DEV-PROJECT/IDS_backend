package com.spoti.api.auth.domain.oauth2.socialShare.repository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spoti.api.auth.domain.oauth2.socialShare.domain.Content;

/**
 * 컨텐츠 정보에 접근하는 레포지토리
 */
@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

	/**
	 * 제목으로 컨텐츠 검색
	 */
	List<Content> findByTitleContaining(String title);

	/**
	 * 작성자 ID로 컨텐츠 목록 조회
	 */
	List<Content> findByAuthorId(Long authorId);

	/**
	 * 조회수 순으로 인기 컨텐츠 조회
	 */
	@Query("SELECT c FROM Content c ORDER BY c.viewCount DESC")
	List<Content> findPopularContents(Pageable pageable);

	/**
	 * 조회수 증가
	 */
	@Query("UPDATE Content c SET c.viewCount = c.viewCount + 1 WHERE c.id = :contentId")
	void incrementViewCount(@Param("contentId") Long contentId);

	/**
	 * 특정 기간 내에 공유가 많이 된 컨텐츠 조회
	 */
	@Query(value = "SELECT c.* FROM contents c " +
		"JOIN share_statistics s ON c.id = s.content_id " +
		"WHERE s.shared_at BETWEEN :startDate AND :endDate " +
		"GROUP BY c.id " +
		"ORDER BY COUNT(s.id) DESC " +
		"LIMIT :limit",
		nativeQuery = true)
	List<Content> findMostSharedContents(
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate,
		@Param("limit") int limit);
}
