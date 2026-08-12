package ktb.ayden.springboot.repository;

import ktb.ayden.springboot.entity.UserPostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPostLikeRepository extends JpaRepository<UserPostLike,Long>{
    boolean existsByUser_UserIdAndPost_PostId(
            Long userId,
            Long postId
    );
    Optional<UserPostLike> findByUser_UserIdAndPost_PostId(
            Long userId,
            Long postId
    );
    long countByPost_PostId(Long postId);
}
