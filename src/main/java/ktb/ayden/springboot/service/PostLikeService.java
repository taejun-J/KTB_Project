package ktb.ayden.springboot.service;

import ktb.ayden.springboot.common.EntityStatus;
import ktb.ayden.springboot.common.exception.CustomException;
import ktb.ayden.springboot.common.exception.ErrorCode;
import ktb.ayden.springboot.entity.Post;
import ktb.ayden.springboot.entity.User;
import ktb.ayden.springboot.entity.UserPostLike;
import ktb.ayden.springboot.repository.PostRepository;
import ktb.ayden.springboot.repository.UserPostLikeRepository;
import ktb.ayden.springboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserPostLikeRepository userPostLikeRepository;

//    @Transactional
//    public void addLike(Long userId, Long postId) {
//        User user = userRepository
//                .findByUserIdAndStatus(userId, EntityStatus.ACTIVE)
//                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
//
//        Post post = postRepository
//                .findByPostIdAndStatus(postId, EntityStatus.ACTIVE)
//                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
//
//        if (userPostLikeRepository
//                .existsByUser_UserIdAndPost_PostId(userId, postId)) {
//            throw new CustomException(ErrorCode.ALREADY_LIKED_POST);
//        }
//
//        try {
//            userPostLikeRepository.save(
//                    new UserPostLike(user, post)
//            );
//        } catch (DataIntegrityViolationException exception) {
//            throw new CustomException(ErrorCode.ALREADY_LIKED_POST);
//        }
//    }
//
//    @Transactional
//    public void deleteLike(Long userId, Long postId) {
//        UserPostLike postLike = userPostLikeRepository
//                .findByUser_UserIdAndPost_PostId(userId, postId)
//                .orElseThrow(() ->
//                        new CustomException(ErrorCode.POST_LIKE_NOT_FOUND)
//                );
//
//        userPostLikeRepository.delete(postLike);
//    }
@Transactional
public long addLike(Long userId, Long postId) {
    User user = userRepository
            .findByUserIdAndStatus(userId, EntityStatus.ACTIVE)
            .orElseThrow(() ->
                    new CustomException(ErrorCode.USER_NOT_FOUND)
            );

    Post post = postRepository
            .findByPostIdAndStatus(postId, EntityStatus.ACTIVE)
            .orElseThrow(() ->
                    new CustomException(ErrorCode.POST_NOT_FOUND)
            );

    if (userPostLikeRepository
            .existsByUser_UserIdAndPost_PostId(userId, postId)) {
        throw new CustomException(ErrorCode.ALREADY_LIKED_POST);
    }

    try {
        userPostLikeRepository.saveAndFlush(
                new UserPostLike(user, post)
        );
    } catch (DataIntegrityViolationException exception) {
        throw new CustomException(ErrorCode.ALREADY_LIKED_POST);
    }

    return userPostLikeRepository.countByPost_PostId(postId);
}

    @Transactional
    public long deleteLike(Long userId, Long postId) {
        UserPostLike postLike = userPostLikeRepository
                .findByUser_UserIdAndPost_PostId(userId, postId)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.POST_LIKE_NOT_FOUND)
                );

        userPostLikeRepository.delete(postLike);
        userPostLikeRepository.flush();

        return userPostLikeRepository.countByPost_PostId(postId);
    }

}