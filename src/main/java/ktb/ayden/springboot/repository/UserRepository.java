package ktb.ayden.springboot.repository;

import ktb.ayden.springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//괄호 안은 <관리할 엔터티, PK타입>
public interface UserRepository extends JpaRepository<User, Long>{
    //로그인시 회원을 이메일로 찾기 위해
    Optional<User> findByEmail(String email);
}
