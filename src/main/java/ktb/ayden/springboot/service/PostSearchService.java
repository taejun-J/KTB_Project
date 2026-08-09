package ktb.ayden.springboot.service;

import co.elastic.clients.elasticsearch._types.SortOrder;
import ktb.ayden.springboot.common.EntityStatus;
import ktb.ayden.springboot.document.PostDocument;
import ktb.ayden.springboot.entity.Post;
import ktb.ayden.springboot.repository.PostSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostSearchService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final PostSearchRepository postSearchRepository;


    // Elasticsearch에서 검색 후 postId만 반환
    public List<Long> searchPostIds(String keyword) {

        NativeQuery query = NativeQuery.builder()

                .withQuery(q -> q
                        .bool(b -> b

                                // 제목 검색
                                .must(m -> m
                                        .match(match -> match
                                                .field("postName")
                                                .query(keyword)
                                        )
                                )

                                // ACTIVE 게시글만 검색
                                .filter(f -> f
                                        .term(term -> term
                                                .field("status")
                                                .value(v ->
                                                        v.stringValue(
                                                                EntityStatus.ACTIVE.name()
                                                        )
                                                )
                                        )
                                )
                        )
                )

                // 최신 게시글부터
                .withSort(s -> s
                        .field(f -> f
                                .field("postId")
                                .order(SortOrder.Desc)
                        )
                )

                // 최대 20개
                .withPageable(PageRequest.of(0, 20))

                .build();


        SearchHits<PostDocument> searchHits =
                elasticsearchOperations.search(
                        query,
                        PostDocument.class
                );


        return searchHits.getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .map(PostDocument::getPostId)
                .toList();
    }


    // MySQL Post 정보를 Elasticsearch에도 저장/갱신
    public void syncPost(Post post) {

        PostDocument document =
                PostDocument.builder()
                        .postId(post.getPostId())
                        .postName(post.getPostName())
                        .status(post.getStatus().name())
                        .build();

        postSearchRepository.save(document);
    }
}