package com.spig.spig.global.security.token;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

// JWT 내부에서 사용하는 커스텀 Claims 이름을 관리
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JWTClaimConstants {
    public static final String ROLES = "roles";

    public static final String TOKEN_TYPE = "token_type";

    public static final String ACCESS_TOKEN = "access";
}