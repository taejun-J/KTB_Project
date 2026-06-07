package ktb.ayden.springboot.repository;

import ktb.ayden.springboot.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    //저장된 리프레시 토큰 조회
    Optional<RefreshToken> findByToken(String token);
    //사용자 기준 → 리프레시 토큰 제거 RTR 흐름 지원
    void deleteByUserId(Long userId);
}
