package ktb.ayden.springboot.repository;

import ktb.ayden.springboot.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
//괄호 안은 <관리할 엔터티, PK타입>
public interface PostRepository extends JpaRepository<Post, Long> {
}
