package com.example.web.recommending.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "mbti_songs")
public class MBTISong {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "mbti_type", nullable = false)
	private String mbtiType;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String artist;

	@Column(name = "spotify_id", nullable = false)
	private String spotifyId;

	@Column(name = "cover_image", nullable = false)
	private String coverImage;

	public MBTISong() {}
}
