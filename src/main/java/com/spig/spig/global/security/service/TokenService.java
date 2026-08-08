package com.spig.spig.global.security.service;

import com.spig.spig.domain.user.dto.LoginResponseDto;
import com.spig.spig.domain.user.entity.User;
import com.spig.spig.global.security.props.JWTProps;
import com.spig.spig.global.security.token.JWTClaimConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final JWTProps jwtProps;

    // 로그인 시, 토큰 발급
    public LoginResponseDto issueTokens(User user){
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusMillis(jwtProps.getAccessTokenExpiration());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProps.getIssuer())
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .subject(user.getId().toString())
                .claim(JWTClaimConstants.ROLES, List.of(user.getRole().name()))
                .claim(JWTClaimConstants.TOKEN_TYPE,
                        JWTClaimConstants.ACCESS_TOKEN)
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256)
                .type("JWT")
                .build();

        // JWT 문자열 생성
        String accessToken = jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims)
        ).getTokenValue();

        return LoginResponseDto.to(accessToken, user.getRole());
    }
}
