package ktb.ayden.springboot.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;
    private Key key;

    @PostConstruct
    //jwt.secret -> UTF-8 HMAC서명용 키 생성
    public void init() {
        //바이트를 HMAC서명용 Key로 변환
        this.key = Keys.hmacShaKeyFor(
                //secret 가져와서 문자열 -> 바이트로
                jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)
        );
    }

    private String createToken(
            String type,
            Long userId,
            Map<String, Object> claims,
            long expSeconds
    ) {
        Instant now = Instant.now();

        //jwt생성
        return Jwts.builder()
                //토큰의 주인 설정
                .subject(String.valueOf(userId))
                .claim("typ", type)
                .claims(claims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expSeconds)))
                //시그니처 만듦(Header + Payload + secret)
                //수업 예시에서의 인증서? 같은 역할/ 해당 JWT가 우리 서버가 만든 진짜인지 확인하는 도장
                .signWith((SecretKey) key, Jwts.SIG.HS256)
                .compact();
    }

    public String createAccessToken(Long userId, String email, String nickname) {
        return createToken(
                "access",
                userId,
                Map.of("email", email, "nickname", nickname),
                jwtProperties.getAccessTokenExpSeconds()
        );
    }

    public String createRefreshToken(Long userId) {
        return createToken(
                "refresh",
                userId,
                Map.of(),
                jwtProperties.getRefreshTokenExpSeconds()
        );
    }
    //Clams : JWT Payload안의 데이터
    public Jws<Claims> parse(String token) {

        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token);
    }

    public boolean isAccessToken(String token) {
        return "access".equals(parse(token).getPayload().get("typ", String.class));
    }

    //JWT에서 userId 꺼내는 메서드
    public Long getUserId(String token) {
        //parse(token) -> 서명 키로 토큰 검증, 만료나 오류 있으면 예외 발생
        return Long.valueOf(parse(token).getPayload().getSubject());
    }

    public Long getAccessTokenValidityInMilliseconds() {
        return jwtProperties.getAccessTokenExpSeconds() * 1000;
    }
}