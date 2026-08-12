package ktb.ayden.springboot.repository;

import ktb.ayden.springboot.document.PostDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostSearchRepository
        extends ElasticsearchRepository<PostDocument, Long> {
}