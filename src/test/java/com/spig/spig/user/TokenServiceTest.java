package com.spig.spig.user;

import com.spig.spig.domain.user.dto.LoginResponseDto;
import com.spig.spig.domain.user.entity.User;
import com.spig.spig.domain.user.entity.UserRole;
import com.spig.spig.global.security.config.JWTConfig;
import com.spig.spig.global.security.props.JWTProps;
import com.spig.spig.global.security.service.TokenService;
import com.spig.spig.global.security.token.JWTClaimConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenServiceTest {

    private static final String ISSUER = "spig-api";
    private static final int ACCESS_TOKEN_EXPIRATION = 7_200_000;
    private static final String TEST_SECRET = Base64.getEncoder()
            .encodeToString(
                    "0123456789abcdef0123456789abcdef"
                            .getBytes(StandardCharsets.UTF_8)
            );

    private TokenService tokenService;
    private JwtDecoder jwtDecoder;
    private JwtAuthenticationConverter authenticationConverter;

    @BeforeEach
    void setUp() {
        JWTProps jwtProps = new JWTProps();
        jwtProps.setIssuer(ISSUER);
        jwtProps.setSecret(TEST_SECRET);
        jwtProps.setAccessTokenExpiration(ACCESS_TOKEN_EXPIRATION);

        JWTConfig jwtConfig = new JWTConfig();
        SecretKey secretKey = jwtConfig.jwtSecretKey(jwtProps);
        JwtEncoder jwtEncoder = jwtConfig.jwtEncoder(secretKey);

        jwtDecoder = jwtConfig.jwtDecoder(secretKey, jwtProps);
        authenticationConverter =
                jwtConfig.jwtAuthenticationConverter();
        tokenService = new TokenService(jwtEncoder, jwtProps);
    }

    @Test
    @DisplayName("로그인 사용자에게 발급한 Access Token을 검증하고 권한으로 변환한다")
    void issueAndDecodeAccessToken() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .loginId("spig-user")
                .password("encoded-password")
                .name("테스트 사용자")
                .role(UserRole.USER)
                .build();

        LoginResponseDto response = tokenService.issueTokens(user);
        Jwt decodedJwt = jwtDecoder.decode(response.getAccessToken());
        var authentication =
                authenticationConverter.convert(decodedJwt);

        assertNotNull(response.getAccessToken());
        assertFalse(response.getAccessToken().isBlank());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(userId.toString(), decodedJwt.getSubject());
        assertEquals(
                ISSUER,
                decodedJwt.getClaimAsString(JwtClaimNames.ISS)
        );
        assertEquals(
                JWTClaimConstants.ACCESS_TOKEN,
                decodedJwt.getClaimAsString(
                        JWTClaimConstants.TOKEN_TYPE
                )
        );
        assertEquals(
                java.util.List.of("USER"),
                decodedJwt.getClaimAsStringList(
                        JWTClaimConstants.ROLES
                )
        );
        assertEquals(
                Duration.ofMillis(ACCESS_TOKEN_EXPIRATION),
                Duration.between(
                        decodedJwt.getIssuedAt(),
                        decodedJwt.getExpiresAt()
                )
        );

        assertNotNull(authentication);
        assertEquals(userId.toString(), authentication.getName());
        var authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        /*
         * Spring Security 7은 ROLE_USER 외에 Bearer 인증 방식 자체를
         * 나타내는 FACTOR_BEARER 권한도 함께 추가한다.
         */
        assertTrue(authorities.contains("ROLE_USER"));
        assertTrue(authorities.contains("FACTOR_BEARER"));
    }

    @Test
    @DisplayName("저장되지 않아 ID가 없는 사용자에게는 토큰을 발급하지 않는다")
    void rejectUserWithoutId() {
        User user = User.builder()
                .loginId("not-saved-user")
                .password("encoded-password")
                .name("저장 전 사용자")
                .role(UserRole.USER)
                .build();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> tokenService.issueTokens(user)
        );

        assertEquals(
                "Access Token을 발급하려면 저장된 사용자 ID가 필요합니다.",
                exception.getMessage()
        );
    }
}
