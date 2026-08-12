package ktb.ayden.springboot.repository;

import ktb.ayden.springboot.common.EntityStatus;
import ktb.ayden.springboot.dto.PostListResponseDto;
import ktb.ayden.springboot.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

//괄호 안은 <관리할 엔터티, PK타입>
public interface PostRepository extends JpaRepository<Post, Long> {
List<Post> findAllByStatus(EntityStatus status);
Optional<Post> findByPostIdAndStatus(Long postId, EntityStatus status);

//원자적 UPDATE
// modifying을 붙임으로 UPDATE,DELETE 쿼리임을 명시 , clear는 update후 기존꺼를 날려서 새로운 값 받게
@Modifying(clearAutomatically = true)
@Query("""
    UPDATE Post p
    SET p.postViewCount = p.postViewCount + 1
    WHERE p.postId = :postId
    AND p.status = :status
""")
long increaseViewCount(
        @Param("postId") Long postId,
        @Param("status") EntityStatus status
);

//목록 조회 + 좋아요수/댓글수 집계를 한 쿼리로
//기존 findAllByStatus는 카운트가 없어서 Post의 0짜리 컬럼이 그대로 나갔음
@Query("""
    select new ktb.ayden.springboot.dto.PostListResponseDto(
        p.postId,
        p.postName,
        u.nickName,
        (select count(l) from UserPostLike l where l.post = p),
        (select count(c) from Comment c where c.commentedPost = p and c.status = :status),
        p.postViewCount,
        p.status,
        p.createdAt,
        p.updatedAt,
        u.profileImage
    )
    from Post p
    join p.postedUser u
    where p.status = :status
""")
List<PostListResponseDto> findPostListWithCounts(@Param("status") EntityStatus status);
}