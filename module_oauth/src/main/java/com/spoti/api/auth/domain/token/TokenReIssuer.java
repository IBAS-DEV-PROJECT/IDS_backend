//리프레시 토큰을 이용해 새로운 액세스 토큰을 발급하는 기능
package com.spoti.api.auth.domain.token;

import com.spoti.api.auth.domain.token.exception.InvalidTokenException;

public interface TokenReIssuer {

    TokenDto reissueAccessToken(String refreshToken) throws InvalidTokenException;
}
