package ktb.ayden.springboot.controller;

import ktb.ayden.springboot.common.response.ApiResponse;
import ktb.ayden.springboot.dto.PostDetailResponseDto;
import ktb.ayden.springboot.dto.PostListResponseDto;
import ktb.ayden.springboot.dto.PostRequestDto;
import ktb.ayden.springboot.dto.UserResponseDto;
import ktb.ayden.springboot.entity.Post;
import ktb.ayden.springboot.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    //1.게시글 추가
    @PostMapping
    //201응답 반환하도록 추가
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PostDetailResponseDto> addPost(
            //RequestAttribute -> 필터가 서버 내부에서 넣어준 값 / 서버가 토큰에서 확인한 로그인 사용자
            @RequestAttribute("userId") Long userId,
            @RequestBody PostRequestDto request
            ){
        PostDetailResponseDto res = postService.addPost(userId,request);
        return ApiResponse.success(res,"게시글 추가 성공");
    }
    //2. 게시글 목록 조회
    @GetMapping()
    //리스트 조회니까 여기도 반환타입 맞춰야 함
    public ApiResponse<List<PostListResponseDto>> getPostList(){
        List<PostListResponseDto> res = postService.getPostList();
        //DB에서 조회한 게시글 엔티티를 DTO로 변환하면 List<PostListResponseDto>
        return ApiResponse.success(res,"게시글 목록 조회");
    }
    //3. 게시글 상세 조회
    @GetMapping("/{postId}")
//    public ApiResponse<PostDetailResponseDto>getPostDetail(@PathVariable Long postId){
    //비로그인 조회도 허용해야 하므로 required=false -> 토큰 없으면 userId가 null로 들어옴
    public ApiResponse<PostDetailResponseDto>getPostDetail(
            @RequestAttribute(value = "userId", required = false) Long userId,
            @PathVariable Long postId){
//        PostDetailResponseDto res = postService.getPostDetail(postId);
        PostDetailResponseDto res = postService.getPostDetail(userId, postId);
        return ApiResponse.success(res,"게시글 상세 조회 성공");
    }
    //4. 게시글 정보 변경
    @PatchMapping ("/{postId}")
    public ApiResponse<PostDetailResponseDto> updatePost(@RequestAttribute("userId") Long userId, @PathVariable Long postId,@RequestBody PostRequestDto request) {
        PostDetailResponseDto res =  postService.updatePost(userId,postId,request);
        return ApiResponse.success(res,"게시글 정보 변경 성공");
    }

    //5. 게시글 삭제
    @DeleteMapping("/{postId}")
    public ApiResponse<PostDetailResponseDto> softDeletePost(@PathVariable Long postId, @RequestAttribute("userId")Long userId){
        PostDetailResponseDto res = postService.softDeletePost(userId,postId);
        return ApiResponse.success(res,"게시글 삭제 성공");
    }
    //6. 첨부파일 업로드
    @PostMapping(
            value = "/upload/attach",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ApiResponse<String> uploadAttach(
            @RequestPart("postFile") MultipartFile postFile
    ) {
        String fileUrl =
                postService.uploadPostImage(postFile);

        return ApiResponse.success(
                fileUrl,
                "첨부파일 업로드 성공"
        );
    }
}



