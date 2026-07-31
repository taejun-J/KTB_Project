package ktb.ayden.springboot.dto;

import ktb.ayden.springboot.common.EntityStatus;
import ktb.ayden.springboot.entity.Comment;
import ktb.ayden.springboot.repository.CommentRepository;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long commentId;
    private String commentText;

    private Long postId;
    private Long userId;
    private String nickName;

    private EntityStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentResponseDto(Comment comment){
        this.commentId = comment.getCommentId();
        this.commentText = comment.getCommentText();

        this.postId = comment.getCommentedPost().getPostId();
        this.userId = comment.getCommentedUser().getUserId();
        this.nickName = comment.getCommentedUser().getNickName();

        this.status = comment.getStatus();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}
