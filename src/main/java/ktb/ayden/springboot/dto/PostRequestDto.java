package ktb.ayden.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
//게시글 작성(post)고려
public class PostRequestDto {
    @NotBlank(message = "게시글 제목을 입력해주세요.")
    private String postName;

    @NotBlank(message = "게시글 내용을 입력해주세요.")
    private String postContent;

    // 첨부파일이 없을 수도 있음
    private String postImage;
}

