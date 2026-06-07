package ktb.ayden.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ktb.ayden.springboot.entity.User;

@Getter
//모든 필드를 매개변수로 받는 생성자를 자동 생성해주는 어노테이션
@AllArgsConstructor
public class LoginResponseDto {
    private User user;
    private TokenInformationDto token;

    public static LoginResponseDto of(
            User user,
            String accessToken,
            long expiresIn
    ) {
        return new LoginResponseDto(
                user,
                new TokenInformationDto(accessToken, expiresIn)
        );
    }
}
