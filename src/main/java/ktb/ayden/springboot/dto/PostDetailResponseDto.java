package ktb.ayden.springboot.dto;

import ktb.ayden.springboot.common.EntityStatus;
import ktb.ayden.springboot.entity.Post;
import ktb.ayden.springboot.entity.UserPostLike;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostDetailResponseDto {
    private Long postId;
    private String postImage;
    private String postName;
    private String postUser;
    private Long postLikesCount;
//    private boolean isLiked;
    //primitive boolean이면 Lombok이 isLiked() 게터를 만들고 Jackson이 JSON 키를 liked로 내림
    //래퍼 Boolean을 쓰면 getIsLiked() -> JSON 키가 isLiked로 유지됨
    private Boolean isLiked;
    private Long postCommentCount;
    private Long postViewCount;
    private String postContent;
    private EntityStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

//    public PostDetailResponseDto(Post post, long postLikesCount){
    //좋아요 여부를 응답에 실어야 프론트가 새로고침 후에도 토글 방향을 알 수 있음
    public PostDetailResponseDto(Post post, long postLikesCount, boolean isLiked){
        this.postId = post.getPostId();
        this.postImage = post.getPostImage();
        this.postName = post.getPostName();
        //User엔티티에서 닉네임 받아오기 postedUser -> nickName받기
        this.postUser = post.getPostedUser().getNickName();
        this.postLikesCount = postLikesCount;
//        this.isLiked = isLiked;
        this.isLiked = isLiked;
        this.postCommentCount = post.getPostCommentCount();
        this.postViewCount = post.getPostViewCount();
        this.postContent = post.getPostContent();
        this.status = post.getStatus();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();

    }
}
