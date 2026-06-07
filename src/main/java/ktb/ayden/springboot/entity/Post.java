package ktb.ayden.springboot.entity;

import jakarta.persistence.*;
import ktb.ayden.springboot.common.entityStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private entityStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //연관관계 표시 - User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    private User postedUser;

    //생성자
    public Post(String postName, String postContent, String postImage){
        this. postName = postName;
        this.postLikesCount = postLikesCount;
        this.postCommentCount = postCommentCount;
        this.postViewCount = postViewCount;
        this.postContent = postContent;
        this.postImage = postImage;
        this.status = entityStatus.ACTIVE;
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
    public void changePostStatus(entityStatus status){
        this.status = status;
    }

}
