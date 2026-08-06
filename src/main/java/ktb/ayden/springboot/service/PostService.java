package ktb.ayden.springboot.service;

//import jakarta.transaction.Transactional;
import ktb.ayden.springboot.common.exception.CustomException;
import ktb.ayden.springboot.common.exception.ErrorCode;
import ktb.ayden.springboot.repository.UserPostLikeRepository;
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
    private final UserPostLikeRepository userPostLikeRepository;


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
        Long postId = post.getPostId();
        Long postLikeCount =
                userPostLikeRepository.countByPost_PostId(postId);
        boolean isLiked = userId != null && userPostLikeRepository.existsByUser_UserIdAndPost_PostId(userId,postId);
//        return new PostDetailResponseDto(savedPost,postLikeCount);
        return new PostDetailResponseDto(savedPost,postLikeCount,isLiked);
    }

    //2. 게시글 목록 조회
    @Transactional(readOnly = true)
    //리스트 조회니까 여기도 반환타입 맞춰야 함
    public List<PostListResponseDto> getPostList(){
//
        //좋아요수/댓글수까지 DB에서 집계해서 한 번에 받아옴
        return postRepository.findPostListWithCounts(EntityStatus.ACTIVE);
    }

    //3. 게시글 상세 조회
    //조회수 계산로직 추가로 readonly 삭제
    @Transactional
    //좋아요 여부 판단에 userId가 필요 -> 비로그인 조회도 허용하므로 null 허용
    public PostDetailResponseDto getPostDetail(Long userId, Long postId){
        //조회수
        Long res = postRepository.increaseViewCount(postId, EntityStatus.ACTIVE);
            if(res == 0) {
                throw new CustomException(ErrorCode.POST_NOT_FOUND);
            }
        Post post = postRepository.findByPostIdAndStatus(postId, EntityStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        //좋아요
        Long postLikeCount = userPostLikeRepository.countByPost_PostId(postId);
        //좋아요 여부
        boolean isLiked = userId != null && userPostLikeRepository.existsByUser_UserIdAndPost_PostId(userId, postId);
//
        return new PostDetailResponseDto(post,postLikeCount,isLiked);

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
        Long postLikeCount =
                userPostLikeRepository.countByPost_PostId(postId);
        boolean isLiked = userId != null && userPostLikeRepository.existsByUser_UserIdAndPost_PostId(userId,postId);
//        return new PostDetailResponseDto(post,postLikeCount);
        return new PostDetailResponseDto(post,postLikeCount,isLiked);
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
        Long postLikeCount =
                userPostLikeRepository.countByPost_PostId(postId);
        boolean isLiked = userId != null && userPostLikeRepository.existsByUser_UserIdAndPost_PostId(userId,postId);
//        return new PostDetailResponseDto(post,postLikeCount);
        return new PostDetailResponseDto(post,postLikeCount,isLiked);

    }

}
