//package ktb.ayden.springboot.service;
//
//import ktb.ayden.springboot.document.PostDocument;
//import ktb.ayden.springboot.dto.PostSearchBenchmarkDto;
//import ktb.ayden.springboot.repository.PostRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class PostSearchBenchmarkService {
//
//    private final PostRepository postRepository;
//    private final PostSearchService postSearchService;
//
//
//    // 1. LIKE
//    @Transactional(readOnly = true)
//    public List<PostSearchBenchmarkDto> searchLike(String keyword) {
//
//        return postRepository.benchmarkLike(keyword)
//                .stream()
//                .map(post -> new PostSearchBenchmarkDto(
//                        post.getPostId(),
//                        post.getPostName(),
//                        post.getStatus()
//                ))
//                .toList();
//    }
//
//
//    // 2. FULLTEXT
//    @Transactional(readOnly = true)
//    public List<PostSearchBenchmarkDto> searchFullText(String keyword) {
//
//        return postRepository.benchmarkFullText(keyword)
//                .stream()
//                .map(post -> new PostSearchBenchmarkDto(
//                        post.getPostId(),
//                        post.getPostName(),
//                        post.getStatus()
//                ))
//                .toList();
//    }
//
//
//    // 3. Elasticsearch
//    public List<PostSearchBenchmarkDto> searchElasticsearch(
//            String keyword
//    ) {
//
//        List<PostDocument> posts =
//                postSearchService.search(keyword);
//
//        return posts.stream()
//                .map(post -> new PostSearchBenchmarkDto(
//                        post.getPostId(),
//                        post.getPostName(),
//                        post.getStatus()
//                ))
//                .toList();
//    }
//}