package com.spoti.api.auth.domain.token.jwtUtils.refreshToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// JPA를 사용하여 데이터베이스에 RefreshToken을 저장하고 관리
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	boolean existsByRefreshToken(String refreshToken);

	void deleteByRefreshToken(String refreshToken); //중복 토큰 삭제 로직(추가)
}
