package ktb.ayden.springboot.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "posts")
public class PostDocument {

    @Id
    private Long postId;

    //text는 검색대상 문자열(es가 분석해서 검색 가능한 토큰으로 만듦)
    @Field(type = FieldType.Text)
    private String postName;

    @Field(type = FieldType.Keyword)
    private String status;
}