package ktb.ayden.springboot.service;

//import jakarta.transaction.Transactional;
import ktb.ayden.springboot.common.exception.CustomException;
import ktb.ayden.springboot.common.exception.ErrorCode;
import org.springframework.transaction.annotation.Transactional;
import ktb.ayden.springboot.common.EntityStatus;
import ktb.ayden.springboot.dto.PostDetailResponseDto;
import ktb.ayden.springboot.dto.PostListResponseDto;
import ktb.ayden.springboot.dto.PostRequestDto;
import ktb.ayden.springboot.entity.Post;
import ktb.ayden.springboot.entity.User;
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
    public PostDetailResponseDto addPost(Long userId, PostRequestDto request){
        User user = userRepository.findByUserIdAndStatus(userId, EntityStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Post post = new Post(
                request.getPostName(),
                request.getPostContent(),
                request.getPostImage(),
                user
        );
        Post savedPost = postRepository.save(post);
        return new PostDetailResponseDto(savedPost);
    }

    //2. 게시글 목록 조회
    @Transactional(readOnly = true)
    //리스트 조회니까 여기도 반환타입 맞춰야 함
    public List<PostListResponseDto> getPostList(){
        List<Post> postList = postRepository.findAllByStatus(EntityStatus.ACTIVE);
        //게시글 목록 DTO들을 담을 빈 result 리스트 생성
        List<PostListResponseDto> result = new ArrayList<>();

        for(Post post : postList){
            result.add(new PostListResponseDto(post));
        }
        return result;
    }

    //3. 게시글 상세 조회
    @Transactional(readOnly = true)
    public PostDetailResponseDto getPostDetail(Long postId){
        Post post = postRepository.findByPostIdAndStatus(postId, EntityStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        return new PostDetailResponseDto(post);
    }

    //4. 게시글 수정
    @Transactional
    public PostDetailResponseDto updatePost(Long userId, Long postId, PostRequestDto request) {
        Post post = postRepository.findByPostIdAndStatus(postId, EntityStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (!post.getPostedUser().getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_USER);
        }
            post.changePostInformation(
                    request.getPostName(),
                    request.getPostContent(),
                    request.getPostImage()
            );
            return new PostDetailResponseDto(post);
        }

    //5. 게시글 삭제(소프트딜리트)
    @Transactional
    public PostDetailResponseDto softDeletePost(Long userId, Long postId){
        Post post = postRepository.findByPostIdAndStatus(postId, EntityStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (!post.getPostedUser().getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_USER);
        }
        post.changePostStatus(EntityStatus.INACTIVE);
        return new PostDetailResponseDto(post);

    }

}
