package ktb.ayden.springboot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
//dto에서도 기본 생성자가 필요
@NoArgsConstructor
public class UserRequestDto {
    private String email;
    private String password;
    private String passwordCheck;
    private String nickname;
    private String profileImage;
}
