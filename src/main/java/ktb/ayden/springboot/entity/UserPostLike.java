package ktb.ayden.springboot.entity;

//JPA가 제공하는 DB 매핑 기능들(@Entity, @Id, @Column 등)을 사용하기 위해 가져오는 패키지
import jakarta.persistence.*;
//lombok은 반복적인 코드를 자동으로 만들어주는 라이브러리
import ktb.ayden.springboot.common.EntityStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

//어노테이션은 클래스 선언 위에 붙어야 함
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//복합 유니크 제약조건 설정
@Table(
        name = "user_post_likes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_post_likes_user_post",
                        columnNames = {"user_id", "post_id"}
                )
        }
)
public class UserPostLike {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPostLikeId;

    boolean isLiked;

    //연관관계 user -> post
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name="user_id",
            nullable = false)
    private User user;

    //post -> user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "post_id",
            nullable = false)
    private Post post;

    public UserPostLike(User user, Post post){
        this.user = user;
        this.post = post;
    }
}
