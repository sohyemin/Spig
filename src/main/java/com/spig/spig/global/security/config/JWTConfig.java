package com.spig.spig.global.security.config;

import com.spig.spig.global.security.props.JWTProps;
import com.spig.spig.global.security.token.JWTClaimConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class JWTConfig {

    /**
     * application.yml을 통해 전달받은 Base64 문자열을
     * 실제 JWT 서명에 사용하는 SecretKey로 변환한다.
     */
    @Bean
    public SecretKey jwtSecretKey(JWTProps jwtProps) {
        String encodedSecret = jwtProps.getSecret();

        if (encodedSecret == null || encodedSecret.isBlank()) {
            throw new IllegalStateException(
                    "JWT_SECRET 환경변수가 설정되어 있지 않습니다."
            );
        }

        byte[] keyBytes;

        try {
            /*
             * .env에는 바이너리 키를 직접 저장할 수 없으므로
             * Base64 문자열로 저장하고 여기에서 디코딩한다.
             */
            keyBytes = Base64.getDecoder().decode(encodedSecret);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException(
                    "JWT_SECRET은 올바른 Base64 형식이어야 합니다.",
                    exception
            );
        }

        /*
         * HS256은 최소 256비트, 즉 32바이트 이상의 키를 사용한다.
         */
        if (keyBytes.length < 32) {
            throw new IllegalStateException(
                    "JWT_SECRET은 Base64 디코딩 후 최소 32바이트여야 합니다."
            );
        }

        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    /**
     * 로그인 성공 시 Access Token을 생성하고 서명한다.
     *
     * JWTService는 JwtEncoder 인터페이스에만 의존하기 때문에
     * 나중에 HS256을 RSA 방식으로 변경해도 서비스 코드 변경을 줄일 수 있다.
     */
    @Bean
    public JwtEncoder jwtEncoder(SecretKey jwtSecretKey) {
        return NimbusJwtEncoder
                .withSecretKey(jwtSecretKey)
                .algorithm(MacAlgorithm.HS256)
                .build();
    }

    /**
     * 클라이언트가 보낸 Access Token을 검증한다.
     *
     * 검증 항목:
     * - HS256 서명
     * - 만료 시간(exp)
     * - 사용 가능 시간(nbf)
     * - 발급자(iss)
     * - token_type이 access인지
     */
    @Bean
    public JwtDecoder jwtDecoder(
            SecretKey jwtSecretKey,
            JWTProps jwtProps
    ) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder
                .withSecretKey(jwtSecretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();

        /*
         * 기본 검증:
         * exp, nbf 같은 시간 정보와 issuer를 검증한다.
         */
        OAuth2TokenValidator<Jwt> defaultValidator =
                JwtValidators.createDefaultWithIssuer(
                        jwtProps.getIssuer()
                );

        /*
         * Refresh Token이 일반 API 인증에 사용되지 않도록
         * token_type이 반드시 access인지 추가로 검증한다.
         */
        OAuth2TokenValidator<Jwt> accessTokenValidator =
                new JwtClaimValidator<String>(
                        JWTClaimConstants.TOKEN_TYPE,
                        JWTClaimConstants.ACCESS_TOKEN::equals
                );

        decoder.setJwtValidator(
                new DelegatingOAuth2TokenValidator<>(
                        defaultValidator,
                        accessTokenValidator
                )
        );

        return decoder;
    }

    /**
     * JWT의 roles Claim을 Spring Security 권한으로 변환한다.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter =
                new JwtGrantedAuthoritiesConverter();

        authoritiesConverter.setAuthoritiesClaimName(
                JWTClaimConstants.ROLES
        );
        authoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter authenticationConverter =
                new JwtAuthenticationConverter();

        /*
         * Authentication.getName()이 JWT의 sub 값을 반환하도록 한다.
         * SPig에서는 sub에 사용자의 UUID를 저장한다.
         */
        authenticationConverter.setPrincipalClaimName(
                JwtClaimNames.SUB
        );

        authenticationConverter.setJwtGrantedAuthoritiesConverter(
                authoritiesConverter
        );

        return authenticationConverter;
    }
}