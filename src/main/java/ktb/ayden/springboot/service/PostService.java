package ktb.ayden.springboot.service;

import jakarta.transaction.Transactional;
import ktb.ayden.springboot.common.entityStatus;
import ktb.ayden.springboot.dto.PostDetailResponseDto;
import ktb.ayden.springboot.dto.PostListResponseDto;
import ktb.ayden.springboot.dto.PostRequestDto;
import ktb.ayden.springboot.dto.UserRequestDto;
import ktb.ayden.springboot.entity.Post;
import ktb.ayden.springboot.repository.PostRepository;
import ktb.ayden.springboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //1.게시글 추가
    //인증,인가 구현 후 추가
    @Transactional
    public PostDetailResponseDto addPost(PostRequestDto request){
        Post post = new Post(
                request.getPostName(),
                request.getPostContent(),
                request.getPostImage()
        );
        Post savedPost = postRepository.save(post);
        return new PostDetailResponseDto(savedPost);
    }

    //2. 게시글 목록 조회
    @Transactional
    //리스트 조회니까 여기도 반환타입 맞춰야 함
    public List<PostListResponseDto> getPostList(){
        List<Post> postList = postRepository.findAll();
        //게시글 목록 DTO들을 담을 빈 result 리스트 생성
        List<PostListResponseDto> result = new ArrayList<>();

        for(Post post : postList){
            result.add(new PostListResponseDto(post));
        }
        return result;
    }

    //3. 게시글 상세 조회
    @Transactional
    public PostDetailResponseDto getPostDetail(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("요청한 대상을 찾을 수 없습니다. 주소가 정확한지 다시 한 번 확인해주세요."));
        return new PostDetailResponseDto(post);
    }

    //4. 게시글 수정
    @Transactional
    public PostDetailResponseDto updatePost(Long postId, PostRequestDto request){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("요청한 대상을 찾을 수 없습니다. 주소가 정확한지 다시 한 번 확인해주세요."));
        post.changePostInformation(
                request.getPostName(),
                request.getPostContent(),
                request.getPostImage()
                );
        return new PostDetailResponseDto(post);
    }

    //5. 게시글 삭제(소프트딜리트)
    @Transactional
    public PostDetailResponseDto softDeletePost(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("요청한 대상을 찾을 수 없습니다. 주소가 정확한지 다시 한 번 확인해주세요."));
        post.changePostStatus(entityStatus.INACTIVE);
        return new PostDetailResponseDto(post);

    }

}
