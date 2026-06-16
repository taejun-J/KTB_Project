package ktb.ayden.springboot.entity;


//JPA가 제공하는 DB 매핑 기능들(@Entity, @Id, @Column 등)을 사용하기 위해 가져오는 패키지
import jakarta.persistence.*;
//lombok은 반복적인 코드를 자동으로 만들어주는 라이브러리
import ktb.ayden.springboot.common.EntityStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//어노테이션은 클래스 선언 위에 붙어야 함
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//JPA가 엔티티 객체를 생성할 경우 기본 생성자 사용-> 필수
//기본 생성자, 외부에서 접근X, JPA만 사용하게 하기 위해 protected설정
public class User{
    //@Id => PK라는 것을 표시, @GeneratedValue~ => auto_increment 사용한다는 것
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    //DB에서 매핑할 컬럼지정
    @Column(name = "userId")
    private Long userId;
    private String nickName;
    private String email;
    private String password;
    private String profileImage;
    @Enumerated(EnumType.STRING)
    private EntityStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //생성자
    public User(String email, String password,String nickName, String profileImage){
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.profileImage = profileImage;
        this.status = EntityStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    //연관관계
    @OneToMany(mappedBy = "postedUser")
    List<Post> posts = new ArrayList<>();

    //엔티티의 상태변경 관련 메서드는 엔티티에서
    //회원 정보 변경(프로필사진, 닉네임)
    public void changeUserInformation(String profileImage, String nickName){
        this.profileImage = profileImage;
        this.nickName = nickName;
    }
    //회원 정보 변경(비밀번호)
    public void changeUserPassword(String encodedPassword){
        this.password = encodedPassword;
    }
    //회원 상태 변경(delete)
    public void changeUserStatus(EntityStatus status){
        this.status = status;
    }
}
