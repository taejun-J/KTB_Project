package ktb.ayden.springboot.service;

import ktb.ayden.springboot.document.PostDocument;
import ktb.ayden.springboot.entity.Post;
import ktb.ayden.springboot.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostSearchIndexService {

    private static final int BATCH_SIZE = 1000;

    private final PostRepository postRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public long indexAllPosts() {

        long lastPostId = 0L;
        long indexedCount = 0L;

        while (true) {

            // MySQL에서 1000개씩 조회
            List<Post> posts =
                    postRepository.findByPostIdGreaterThanOrderByPostIdAsc(
                            lastPostId,
                            PageRequest.of(0, BATCH_SIZE)
                    );

            // 더 이상 데이터가 없으면 종료
            if (posts.isEmpty()) {
                break;
            }

            // JPA Entity -> Elasticsearch Document
            List<IndexQuery> indexQueries = posts.stream()
                    .map(post -> {

                        PostDocument document = PostDocument.builder()
                                .postId(post.getPostId())
                                .postName(post.getPostName())
                                .status(post.getStatus().name())
                                .build();

                        return new IndexQueryBuilder()
                                .withId(post.getPostId().toString())
                                .withObject(document)
                                .build();
                    })
                    .toList();

            // 1000개 한 번에 Elasticsearch로 전송
            elasticsearchOperations.bulkIndex(
                    indexQueries,
                    PostDocument.class
            );

            indexedCount += posts.size();

            // 다음 조회 시작점
            lastPostId = posts.get(posts.size() - 1).getPostId();

            System.out.println(
                    "Elasticsearch indexing: " + indexedCount
            );
        }

        // 검색에 즉시 보이도록 refresh
        elasticsearchOperations
                .indexOps(PostDocument.class)
                .refresh();

        return indexedCount;
    }
}