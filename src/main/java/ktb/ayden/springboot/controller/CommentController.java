package ktb.ayden.springboot.controller;

import jakarta.validation.Valid;
import ktb.ayden.springboot.dto.CommentRequestDto;
import ktb.ayden.springboot.dto.CommentResponseDto;
import ktb.ayden.springboot.service.CommentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Getter
//컨트롤러에서 붙여줘야 함 -> 없으면 오류의 원인
@RestController
//final필드 밭는 생성자 자동으로 만드어줌
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    //1. 댓글추가
    @PostMapping
    public CommentResponseDto addComment(
                @Valid @RequestAttribute("userId") Long userId,
                @PathVariable("postId") Long postId,
                @RequestBody CommentRequestDto request
    ){
       return commentService.addComment(postId,userId,request);
    }

    //2. 댓글 조회(읽어오기)
    @GetMapping
    //리스트의 형태로 받아와야함
    public List<CommentResponseDto> readComment(@Valid @PathVariable("postId") Long postId){
        return commentService.getCommentsByPost(postId);
    }
    //3. 댓글 수정하기
    @PatchMapping("/{commentId}")
    public CommentResponseDto changeComment(
            @Valid @PathVariable("commentId") Long commentId,
            @Valid @PathVariable("postId") Long postId,
            @Valid @RequestAttribute("userId") Long userId,
            @RequestBody CommentRequestDto request
    ){
        return commentService.updateComment(commentId,postId,userId,request);
    }
    //4. 댓글 삭제하기
    @DeleteMapping("/{commentId}")
    public CommentResponseDto deleteComment(
            @Valid @RequestAttribute("userId") Long userId,
            @Valid @PathVariable("postId") Long postId,
            @Valid @PathVariable("commentId") Long commentId
    ){
        return commentService.softDeleteComment(userId,postId,commentId);
    }
}
