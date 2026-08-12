package ktb.ayden.springboot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    private String token;
    private Long userId;
    private LocalDateTime expiresAt;

    public RefreshToken(String token, Long userId, LocalDateTime expiresAt){
        this.token = token;
        this.userId = userId;
        this.expiresAt = expiresAt;
    }

    //엔티티 상태 조작은 엔티티에서
    public boolean isExpired(){
        //현 시각을 기준으로 토큰의 만료 여부를 판단
        return LocalDateTime.now().isAfter(this.expiresAt);
    }
}
