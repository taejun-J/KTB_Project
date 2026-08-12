package ktb.ayden.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {
    @NotBlank(message = "닉네임을 입력해주세요")
    @Size(max=10,message = "닉네임은 최대 10글자 입니다.")
    private String nickName;
    private String profileImage;
}
