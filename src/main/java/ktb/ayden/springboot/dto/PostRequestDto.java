package ktb.ayden.springboot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
//게시글 작성(post)고려
public class PostRequestDto {
    private String postName;
    private String postContent;
    private String postImage;
}
