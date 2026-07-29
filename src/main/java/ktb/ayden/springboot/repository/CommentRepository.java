package ktb.ayden.springboot.repository;

import ktb.ayden.springboot.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import ktb.ayden.springboot.entity.Comment;
import ktb.ayden.springboot.common.EntityStatus;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    //comment안에는 post 가 있는거지 postId가 있는게 아님 -> post안으로 들어가서 postId 불러야 함
    @Query("select c from Comment c join fetch c.commentedPost p where p.postId = :postId and c.status = :status")
    List<Comment> findAllByCommentedPost_PostIdAndStatus(Long postId, EntityStatus status);
    //지워지지 않은 comment만 불러와야함
    Optional<Comment> findByCommentIdAndStatus(Long CommentId,EntityStatus status);

    //조건 3개 한 번에
    Optional<Comment>findByCommentIdAndCommentedPost_PostIdAndStatus(
            Long commentId,
            Long postId,
            EntityStatus status
    );
}
