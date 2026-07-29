package ktb.ayden.springboot.controller;

import jakarta.validation.Valid;
import ktb.ayden.springboot.common.response.ApiResponse;
import ktb.ayden.springboot.dto.CommentRequestDto;
import ktb.ayden.springboot.dto.CommentResponseDto;
import ktb.ayden.springboot.service.CommentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    //201응답 반환하도록 추가
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CommentResponseDto> addComment(
                 @RequestAttribute("userId") Long userId,
                 @PathVariable("postId") Long postId,
                 @Valid @RequestBody CommentRequestDto request
    ){
       CommentResponseDto res = commentService.addComment(postId,userId,request);
       return ApiResponse.success(res,"댓글추가완료");
    }

    //2. 댓글 조회(읽어오기)
    @GetMapping
    //리스트의 형태로 받아와야함
    public ApiResponse<List<CommentResponseDto>> readComment(@Valid @PathVariable("postId") Long postId){
        List<CommentResponseDto> res = commentService.getCommentsByPost(postId);
        return ApiResponse.success(res,"댓글 조회 성공");
    }
    //3. 댓글 수정하기
    @PatchMapping("/{commentId}")
    public ApiResponse<CommentResponseDto> changeComment(
             @PathVariable("commentId") Long commentId,
             @PathVariable("postId") Long postId,
             @RequestAttribute("userId") Long userId,
             @Valid @RequestBody CommentRequestDto request
    ){
        CommentResponseDto res = commentService.updateComment(commentId,postId,userId,request);
        return ApiResponse.success(res,"댓글 수정 성공");
    }
    //4. 댓글 삭제하기
    @DeleteMapping("/{commentId}")
    public ApiResponse<CommentResponseDto> deleteComment(
            @RequestAttribute("userId") Long userId,
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId
    ){
        CommentResponseDto res = commentService.softDeleteComment(userId,postId,commentId);
        return ApiResponse.success(res,"댓글 삭제 성공");
    }
}
