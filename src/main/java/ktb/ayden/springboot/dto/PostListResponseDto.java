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
    private String profileImage;


//    public PostListResponseDto(Post post){
//        this.postId = post.getPostId();
//        this.postName = post.getPostName();
//        //User엔티티에서 닉네임 받아오기
//        this.postUser = post.getPostedUser().getNickName();
//        //아래 두 컬럼은 어디서도 증가되지 않아 항상 0 -> 집계값을 직접 받도록 변경
//        this.postLikesCount = post.getPostLikesCount();
//        this.postCommentCount = post.getPostCommentCount();
//        this.postViewCount = post.getPostViewCount();
//        this.status = post.getStatus();
//        this.createdAt = post.getCreatedAt();
//        this.updatedAt = post.getUpdatedAt();
//
//    }

    //JPQL 생성자 표현식용 - 엔티티 대신 값을 직접 받음(DTO 안에서 지연로딩 발생 안 함)
    public PostListResponseDto(
            Long postId,
            String postName,
            String postUser,
            Long postLikesCount,
            Long postCommentCount,
            Long postViewCount,
            EntityStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            String profileImage

    ){

        this.postId = postId;
        this.postName = postName;
        this.postUser = postUser;
        this.postLikesCount = postLikesCount;
        this.postCommentCount = postCommentCount;
        this.postViewCount = postViewCount;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.profileImage = profileImage;
    }
}
