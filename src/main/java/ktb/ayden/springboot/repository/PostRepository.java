package ktb.ayden.springboot.repository;

import ktb.ayden.springboot.common.EntityStatus;
import ktb.ayden.springboot.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//괄호 안은 <관리할 엔터티, PK타입>
public interface PostRepository extends JpaRepository<Post, Long> {
List<Post> findAllByStatus(EntityStatus status);
Optional<Post> findByPostIdAndStatus(Long postId, EntityStatus status);
}
