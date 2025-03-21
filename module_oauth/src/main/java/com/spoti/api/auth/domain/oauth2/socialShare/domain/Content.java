package com.spoti.api.auth.domain.oauth2.socialShare.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;

/**
 * 공유할 컨텐츠 정보를 담는 엔티티
 */
@Setter
@Getter
@Entity
@Table(name = "contents")
public class Content {

	// Getters and Setters
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(length = 1000)
	private String description;

	@Column(name = "thumbnail_url")
	private String thumbnailUrl;

	@Column(name = "content_url")
	private String contentUrl;

	@Column(name = "view_count", columnDefinition = "integer default 0")
	private Integer viewCount = 0;

	// 기본 생성자
	public Content() {
	}

	// 뷰 카운트 증가 메소드
	public void incrementViewCount() {
		this.viewCount++;
	}
}
