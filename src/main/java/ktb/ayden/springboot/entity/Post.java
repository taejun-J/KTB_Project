package ktb.ayden.springboot.entity;

import jakarta.persistence.*;
import ktb.ayden.springboot.common.EntityStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@RequiredArgsConstructor
//@NoArgsConstructor
public class Post {
    //PK 선언 및 auto_increment설정
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postId")
    private Long postId;
    private String postName;
    private Long postLikesCount = 0L;
    private Long postCommentCount = 0L;
    private Long postViewCount = 0L;
    private String postContent;
    private String postImage;
    @Enumerated(EnumType.STRING)
    private EntityStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //연관관계 표시 - User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    private User postedUser;

    //생성자
    //User 객체 전달(관계를 객체로 전달)
    public Post(String postName, String postContent, String postImage, User postedUser){
        this.postedUser = postedUser;
        this. postName = postName;
        //값은 초기화
        this.postLikesCount = 0L;
        this.postCommentCount = 0L;
        this.postViewCount = 0L;
        this.postContent = postContent;
        this.postImage = postImage;
        this.status = EntityStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    //Post 엔티티의 상태변경 메서드는 엔티티에서
    public void changePostInformation(String postName, String postContent, String postImage) {
        this.postName = postName;
        this.postContent = postContent;
        this.postImage = postImage;
    }
    //게시글 상태 변경(delete)
    public void changePostStatus(EntityStatus status){
        this.status = status;
    }

}
