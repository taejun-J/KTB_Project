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
    //댓글은 댓글id를 기준으로 오름차순, 최신(더 높은 id)이 아래에 나오도록
    @Query("select c from Comment c join fetch c.commentedPost p where p.postId = :postId and c.status = :status order by c.commentId asc")
    List<Comment> findAllByCommentedPost_PostIdAndStatusOrderBycommentIdDesc(Long postId, EntityStatus status);
    //지워지지 않은 comment만 불러와야함
    Optional<Comment> findByCommentIdAndStatus(Long CommentId,EntityStatus status);

    //조건 3개 한 번에
    Optional<Comment>findByCommentIdAndCommentedPost_PostIdAndStatus(
            Long commentId,
            Long postId,
            EntityStatus status
    );
}
