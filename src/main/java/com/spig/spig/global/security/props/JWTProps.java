package com.spig.spig.global.security.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.jwt")
public class JWTProps {
    private String issuer;
    private String secret;
    private int accessTokenExpiration;
}
