package com.spig.spig.user;

import com.spig.spig.global.security.config.JWTConfig;
import com.spig.spig.global.security.props.JWTProps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JWTConfigTest {

    private final JWTConfig jwtConfig = new JWTConfig();

    @Test
    @DisplayName("JWT Secret이 비어 있으면 애플리케이션 설정 오류로 처리한다")
    void rejectBlankSecret() {
        JWTProps jwtProps = new JWTProps();
        jwtProps.setSecret(" ");

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> jwtConfig.jwtSecretKey(jwtProps)
        );

        assertEquals(
                "JWT_SECRET 환경변수가 설정되어 있지 않습니다.",
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("JWT Secret이 Base64 형식이 아니면 설정 오류로 처리한다")
    void rejectInvalidBase64Secret() {
        JWTProps jwtProps = new JWTProps();
        jwtProps.setSecret("not-base64!");

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> jwtConfig.jwtSecretKey(jwtProps)
        );

        assertEquals(
                "JWT_SECRET은 올바른 Base64 형식이어야 합니다.",
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("HS256 Secret이 32바이트보다 짧으면 설정 오류로 처리한다")
    void rejectShortSecret() {
        JWTProps jwtProps = new JWTProps();
        jwtProps.setSecret(
                Base64.getEncoder().encodeToString(
                        "short-secret"
                                .getBytes(StandardCharsets.UTF_8)
                )
        );

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> jwtConfig.jwtSecretKey(jwtProps)
        );

        assertEquals(
                "JWT_SECRET은 Base64 디코딩 후 최소 32바이트여야 합니다.",
                exception.getMessage()
        );
    }
}
