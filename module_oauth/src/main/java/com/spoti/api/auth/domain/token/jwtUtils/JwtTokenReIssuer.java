package com.spoti.api.auth.domain.token.jwtUtils;

import lombok.RequiredArgsConstructor;

import com.spoti.api.auth.domain.token.TokenDto;
import com.spoti.api.auth.domain.token.TokenReIssuer;
import com.spoti.api.auth.domain.token.TokenResolver;
import com.spoti.api.auth.domain.token.TokenUtil;
import com.spoti.api.auth.domain.token.exception.InvalidTokenException;
import com.spoti.api.auth.domain.token.jwtUtils.refreshToken.RefreshTokenNotFoundException;
import com.spoti.api.auth.domain.token.jwtUtils.refreshToken.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenReIssuer implements TokenReIssuer {

	private final TokenUtil tokenUtil;
	private final TokenResolver tokenResolver;
	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	public TokenDto reissueAccessToken(String refreshToken)
		throws InvalidTokenException, RefreshTokenNotFoundException {

		tokenUtil.validate(refreshToken);

		if (!refreshTokenRepository.existsByRefreshToken(refreshToken)) {
			throw new RefreshTokenNotFoundException();
		}

		return tokenUtil.reissueAccessTokenUsing(refreshToken);
	}
}
