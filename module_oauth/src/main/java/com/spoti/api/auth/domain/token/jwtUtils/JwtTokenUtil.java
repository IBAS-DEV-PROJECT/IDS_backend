package com.spoti.api.auth.domain.token.jwtUtils;

import com.spoti.api.auth.domain.token.TokenDto;
import com.spoti.api.auth.domain.token.TokenUtil;
import com.spoti.api.auth.domain.token.exception.ExpiredTokenException;
import com.spoti.api.auth.domain.token.exception.InvalidTokenException;
import com.spoti.api.auth.domain.token.jwtUtils.refreshToken.RefreshToken;
import com.spoti.api.auth.domain.token.jwtUtils.refreshToken.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil implements TokenUtil {

	private final RefreshTokenRepository refreshTokenRepository;

	@Value("${jwt.secretKey}")
	private String secretKey;

	private Key signingKey;

	private static final long ACCESS_TOKEN_VALID_MILLISECOND = 30 * 60 * 1000L; // 30분
	private static final long REFRESH_TOKEN_VALID_MILLI_SECOND = 7 * 24 * 60 * 60 * 1000L; // 7일

	private static final String AUTHORITY = "authorities";
	private static final String MEMBER_ID = "memberId";

	@PostConstruct
	public void init() {
		this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}

	/**
	 * 로그인 후 JWT 발급
	 */
	public TokenDto issueTokens(Authentication authentication) {
		String accessToken = createToken(authentication, ACCESS_TOKEN_VALID_MILLISECOND);
		String refreshToken = createToken(authentication, REFRESH_TOKEN_VALID_MILLI_SECOND);

		// 기존 리프레시 토큰 제거 후 새로 저장
		if (refreshTokenRepository.existsByRefreshToken(refreshToken)) {
			refreshTokenRepository.deleteByRefreshToken(refreshToken);
		}
		refreshTokenRepository.save(new RefreshToken(refreshToken));

		return TokenDto.builder()
			.grantType("Bearer")
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.accessTokenExpireDate(ACCESS_TOKEN_VALID_MILLISECOND)
			.build();
	}

	/**
	 * JWT 검증 메서드
	 */
	public void validate(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
		} catch (SecurityException | MalformedJwtException | SignatureException ex) {
			log.error("Invalid JWT signature");
			throw new InvalidTokenException();
		} catch (ExpiredJwtException ex) {
			log.error("Expired JWT token");
			throw new ExpiredTokenException();
		} catch (UnsupportedJwtException | IllegalArgumentException ex) {
			log.error("Unsupported or empty JWT token");
			throw new InvalidTokenException();
		}
	}

	/**
	 * JWT 생성 메서드
	 */
	private String createToken(Authentication authentication, Long expiration) {
		String uid = authentication.getName();
		List<String> authorities = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.toList());

		Date now = new Date();
		return Jwts.builder()
			.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
			.setSubject(uid)
			.claim(MEMBER_ID, uid)
			.claim(AUTHORITY, authorities)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + expiration))
			.signWith(signingKey, SignatureAlgorithm.HS512)
			.compact();
	}

	/**
	 * 리프레시 토큰을 사용하여 새 액세스 토큰 발급
	 */
	public TokenDto reissueAccessTokenUsing(String refreshToken) throws InvalidTokenException {
		if (!refreshTokenRepository.existsByRefreshToken(refreshToken)) {
			throw new InvalidTokenException();
		}

		try {
			Claims claims = parseClaims(refreshToken);
			return createAccessTokenOnly(claims);
		} catch (JwtException e) {
			throw new InvalidTokenException();
		}
	}

	@Override
	public long getExpiration() {
		return 0;
	}

	/**
	 * 새 액세스 토큰 생성 (리프레시 토큰에서 사용자 정보 추출)
	 */
	private TokenDto createAccessTokenOnly(Claims claims) {
		Date now = new Date();

		String newAccessToken = Jwts.builder()
			.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
			.setSubject(claims.getSubject())
			.claim(MEMBER_ID, claims.get(MEMBER_ID))
			.claim(AUTHORITY, claims.get(AUTHORITY))
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_MILLISECOND))
			.signWith(signingKey, SignatureAlgorithm.HS512)
			.compact();

		return TokenDto.builder()
			.grantType("Bearer")
			.accessToken(newAccessToken)
			.refreshToken("")
			.accessTokenExpireDate(ACCESS_TOKEN_VALID_MILLISECOND)
			.build();
	}

	/**
	 * JWT 토큰에서 사용자 인증 정보 추출
	 */
	@Override
	public Authentication getAuthentication(String token) {
		Claims claims = parseClaims(token);
		String username = claims.getSubject();
		List<GrantedAuthority> authorities = ((List<String>) claims.get(AUTHORITY)).stream()
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());

		return new UsernamePasswordAuthenticationToken(username, token, authorities);
	}

	/**
	 * JWT 파싱하여 클레임 정보 가져오기
	 */
	private Claims parseClaims(String token) throws JwtException {
		return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
	}
}
