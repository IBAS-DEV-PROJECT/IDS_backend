package com.spoti.api.auth.domain.oauth2.socialShare.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 소셜 미디어 공유 통계를 저장하는 엔티티
 */
@Entity
@Setter
@Getter
@Table(name = "share_statistics")
public class ShareStatistics {

	// Getters and Setters
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "content_id", nullable = false)
	private Long contentId;

	@Column(nullable = false, length = 50)
	private String platform;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "shared_at", nullable = false)
	private LocalDateTime sharedAt;

	@Column(name = "shared_url", length = 500)
	private String sharedUrl;

	@Column(name = "user_agent", length = 500)
	private String userAgent;

	@Column(name = "ip_address", length = 50)
	private String ipAddress;

	// 기본 생성자
	public ShareStatistics() {
		this.sharedAt = LocalDateTime.now();
	}

	// 주요 필드 생성자
	public ShareStatistics(Long contentId, String platform, Long userId) {
		this.contentId = contentId;
		this.platform = platform;
		this.userId = userId;
		this.sharedAt = LocalDateTime.now();
	}

}
