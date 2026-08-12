package ktb.ayden.springboot.controller;

import ktb.ayden.springboot.common.response.ApiResponse;
import ktb.ayden.springboot.dto.*;

import ktb.ayden.springboot.service.PostLikeService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;



//HTTP요청을 받는 컨트롤러를 의미 <- RestController
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor


public class PostLikeController {
    private final PostLikeService postLikeService;
    //좋아요 추가
    @PostMapping("/{postId}/likes")
    public ApiResponse<Long> addPostLike(
            @RequestAttribute("userId") Long userId,
            @PathVariable("postId") Long postId
    )
    {
        long likeCount =
                postLikeService.addLike(userId, postId);
        return ApiResponse.success(likeCount, "좋아요 추가 완료");
    }

    @DeleteMapping("/{postId}/likes")
    public ApiResponse<Long> deletePostLike(
            @RequestAttribute("userId") Long userId,
            @PathVariable("postId") Long postId
    ){
        long likeCount =
                postLikeService.deleteLike(userId, postId);
        return ApiResponse.success(likeCount,"좋아요 취소 완료");
    }
}
