package com.spoti.api.auth.domain.oauth2.socialShare.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spoti.api.auth.domain.oauth2.socialShare.domain.ShareStatistics;

/**
 * 공유 통계 정보에 접근하는 레포지토리
 */
@Repository
public interface ShareStatisticsRepository extends JpaRepository<ShareStatistics, Long> {

	/**
	 * 컨텐츠 ID별 공유 통계 조회
	 */
	List<ShareStatistics> findByContentId(Long contentId);

	/**
	 * 플랫폼별 공유 통계 조회
	 */
	List<ShareStatistics> findByPlatform(String platform);

	/**
	 * 사용자별 공유 통계 조회
	 */
	List<ShareStatistics> findByUserId(Long userId);

	/**
	 * 특정 기간 내 공유 통계 조회
	 */
	List<ShareStatistics> findBySharedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

	/**
	 * 컨텐츠별 공유 수 집계
	 */
	@Query("SELECT s.contentId, COUNT(s) FROM ShareStatistics s GROUP BY s.contentId ORDER BY COUNT(s) DESC")
	List<Object[]> countSharesByContent();

	/**
	 * 플랫폼별 공유 수 집계
	 */
	@Query("SELECT s.platform, COUNT(s) FROM ShareStatistics s GROUP BY s.platform ORDER BY COUNT(s) DESC")
	List<Object[]> countSharesByPlatform();

	/**
	 * 특정 컨텐츠의 플랫폼별 공유 수 집계
	 */
	@Query("SELECT s.platform, COUNT(s) FROM ShareStatistics s WHERE s.contentId = :contentId GROUP BY s.platform")
	List<Object[]> countSharesByPlatformForContent(@Param("contentId") Long contentId);

	/**
	 * 특정 기간 내 일별 공유 추이
	 */
	@Query(value = "SELECT DATE(s.shared_at) as shareDate, COUNT(s.id) " +
		"FROM share_statistics s " +
		"WHERE s.shared_at BETWEEN :startDate AND :endDate " +
		"GROUP BY DATE(s.shared_at) " +
		"ORDER BY shareDate ASC",
		nativeQuery = true)
	List<Object[]> countSharesByDateBetween(
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate);

}
