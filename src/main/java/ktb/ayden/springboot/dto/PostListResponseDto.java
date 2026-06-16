package ktb.ayden.springboot.dto;

import ktb.ayden.springboot.common.EntityStatus;
import ktb.ayden.springboot.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostListResponseDto {
    private Long postId;
    private String postName;
    private String postUser;
    private Long postLikesCount;
    private Long postCommentCount;
    private Long postViewCount;
    private EntityStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostListResponseDto(Post post){
        this.postId = post.getPostId();
        this.postName = post.getPostName();
        //User엔티티에서 닉네임 받아오기
        this.postUser = post.getPostedUser().getNickName();
        this.postLikesCount = post.getPostLikesCount();
        this.postCommentCount = post.getPostCommentCount();
        this.postViewCount = post.getPostViewCount();
        this.status = post.getStatus();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();

    }
}
