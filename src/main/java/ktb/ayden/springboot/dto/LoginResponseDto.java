package ktb.ayden.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ktb.ayden.springboot.entity.User;

@Getter
//모든 필드를 매개변수로 받는 생성자를 자동 생성해주는 어노테이션
@AllArgsConstructor
public class LoginResponseDto {

    //private User user; -> DTO안에 들어온 엔티티 -> JSON으로 변환 시도 -> 무한 반복 / dto에는 엔티티 직접 넣지 않기
    private UserResponseDto user;
    private TokenInformationDto token;

    public static LoginResponseDto of(
            User user,
            String accessToken,
            long expiresIn
    ) {
        return new LoginResponseDto(
                new UserResponseDto(user),
                new TokenInformationDto(accessToken, expiresIn)
        );
    }
}
