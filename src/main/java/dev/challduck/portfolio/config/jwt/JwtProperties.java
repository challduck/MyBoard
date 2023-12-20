package dev.challduck.portfolio.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("jwt") // application.properties를 가져와서 사용하는 어노테이션
public class JwtProperties {
    private String issuer;
    private String secretKey;
}
