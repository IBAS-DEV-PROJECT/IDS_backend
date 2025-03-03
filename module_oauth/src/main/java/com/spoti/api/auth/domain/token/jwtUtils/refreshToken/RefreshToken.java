package com.spoti.api.auth.domain.token.jwtUtils.refreshToken;


import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //직접 new RefreshToken()으로 생성하지 못하도록 보호
public class RefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "TEXT")
	private String refreshToken;

	private LocalDateTime created;

	public RefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
		this.created = LocalDateTime.now();
	}
}
