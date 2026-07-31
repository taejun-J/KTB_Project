package ktb.ayden.springboot.service;

import ktb.ayden.springboot.common.EntityStatus;
import ktb.ayden.springboot.dto.CommentRequestDto;
import ktb.ayden.springboot.dto.CommentResponseDto;
import ktb.ayden.springboot.entity.Comment;
import ktb.ayden.springboot.entity.Post;
import ktb.ayden.springboot.entity.User;
import ktb.ayden.springboot.common.exception.CustomException;
import ktb.ayden.springboot.common.exception.ErrorCode;
import ktb.ayden.springboot.repository.CommentRepository;
import ktb.ayden.springboot.repository.PostRepository;
import ktb.ayden.springboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    //댓글추가하기
    //유저 및 게시글 존재 여부 검증 -> 작성
    public CommentResponseDto addComment(Long postId, Long userId, CommentRequestDto request) {

        Post post = postRepository.findByPostIdAndStatus(postId,EntityStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        User user = userRepository.findByUserIdAndStatus(userId,EntityStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Comment comment = new Comment(
                request.getCommentText(),
                user,
                post
        );

        Comment savedComment = commentRepository.save(comment);

        return new CommentResponseDto(savedComment);
    }
    //조회에는 Readonly
    //게시글의 댓글 받아오기
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByPost(Long postId) {

        if (!postRepository.existsById(postId)) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }

        return commentRepository.findAllByCommentedPost_PostIdAndStatus(postId, EntityStatus.ACTIVE)
                //for문과 동일
                //리스트를 하나씩 꺼내서 처리할 준비
                .stream()
                //comment -> new commentResponseDto
                .map(CommentResponseDto::new)
                //다시 묶어주기
                .toList();
    }

    //댓글수정
    @Transactional
    public CommentResponseDto updateComment(Long commentId, Long postId, Long userId, CommentRequestDto request) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        // 작성자 검증
        if (!comment.getCommentedUser().getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_USER);
        }
            // 댓글이 해당 게시글 소속인지
            if (!comment.getCommentedPost().getPostId().equals(postId)) {
                throw new CustomException(ErrorCode.POST_NOT_FOUND);
            }

            comment.changeCommentContent(request.getCommentText());
            return new CommentResponseDto(comment);
        }

    //댓글 삭제
    @Transactional
    public CommentResponseDto softDeleteComment(Long userId, Long postId, Long commentId) {
        Comment comment = commentRepository.findByCommentIdAndStatus(commentId,EntityStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        //우선 해당 게시글이 존재하는게 맞는지 확인
       Post post = postRepository.findByPostIdAndStatus(postId,EntityStatus.ACTIVE)
               .orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_FOUND));
       //댓글이 해당 게시글에 속한게 맞는지 확인
        if(!comment.getCommentedPost().getPostId().equals(postId)){
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }
       // 댓글 작성자가 맞는지 확인 (로그인 유저, 댓글 작성자 id비교)
        if(!comment.getCommentedUser().getUserId().equals(userId)){
            throw new CustomException(ErrorCode.FORBIDDEN_USER);
        }
        comment.changeCommentStatus(EntityStatus.INACTIVE);
        return new CommentResponseDto(comment);
    }
  }
