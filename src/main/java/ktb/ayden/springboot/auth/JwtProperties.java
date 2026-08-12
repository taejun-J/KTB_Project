package ktb.ayden.springboot.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
//application.yml에서 jwt 아래에 있는 값을 이 객체에 넣어라
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret;
    private long accessTokenExpSeconds;
    private long refreshTokenExpSeconds;
}
