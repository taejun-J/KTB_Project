package ktb.ayden.springboot.controller;

import ktb.ayden.springboot.dto.PostDetailResponseDto;
import ktb.ayden.springboot.dto.PostListResponseDto;
import ktb.ayden.springboot.dto.PostRequestDto;
import ktb.ayden.springboot.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    //1.게시글 추가
    @PostMapping
    public PostDetailResponseDto addPost(@RequestBody PostRequestDto request){
        return postService.addPost(request);
    }
    //2. 게시글 목록 조회
    @GetMapping()
    //리스트 조회니까 여기도 반환타입 맞춰야 함
    public List<PostListResponseDto> getPostList(){
        return postService.getPostList();
        //DB에서 조회한 게시글 엔티티를 DTO로 변환하면 List<PostListResponseDto>
    }
    //3. 게시글 상세 조회
    @GetMapping("/{postId}")
    public PostDetailResponseDto getPostDetail(@PathVariable Long postId){
        return postService.getPostDetail(postId);
    }
    //4. 게시글 정보 변경
    @PatchMapping ("/{postId}")
    public PostDetailResponseDto updatePost(@PathVariable Long postId,@RequestBody PostRequestDto request) {
        return postService.updatePost(postId,request);
    }

    //5. 게시글 삭제
    @DeleteMapping("/{postId}")
    public PostDetailResponseDto softDeletePost(@PathVariable Long postId){
        return postService.softDeletePost(postId);
    }


}
