package ktb.ayden.springboot.repository;

import ktb.ayden.springboot.common.EntityStatus;
import ktb.ayden.springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//괄호 안은 <관리할 엔터티, PK타입>
    //pk기준으로 찾는거는 안적어도 됨 ex. findById
public interface UserRepository extends JpaRepository<User, Long>{
    //로그인시 회원을 이메일로 찾기 위해 -> 삭제
//    Optional<User> findByEmail(String email);
    //소프트 딜리트 대응
    Optional<User> findByEmailAndStatus(String email, EntityStatus status);
    Optional<User> findByUserIdAndStatus(Long userId, EntityStatus status);
    //중복 이메일 대응
    boolean existsByEmail(String email);
}
