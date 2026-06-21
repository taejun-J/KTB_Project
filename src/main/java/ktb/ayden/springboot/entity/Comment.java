package ktb.ayden.springboot.entity;


import jakarta.persistence.*;
import ktb.ayden.springboot.common.EntityStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentId")
    private Long commentId;
    private String commentText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    private EntityStatus status;

    //연관관계 표시 - User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    private User commentedUser;

    //연관관계 표시 - Post
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="postId")
    private Post commentedPost;

    //생성자
    public Comment(String commentText, User commentedUser, Post commentedPost){
        this.commentText = commentText;
        this.commentedUser = commentedUser;
        this.commentedPost = commentedPost;
        this.status = EntityStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt= LocalDateTime.now();
    }
    //Comment엔티티의 변경 메서드
    public void changeCommentContent(String commentText){
        this.commentText = commentText;
        this.updatedAt= LocalDateTime.now();
    }
    //Comment삭제
    public void changeCommentStatus(EntityStatus status){
        this.status = status;
    }
}
