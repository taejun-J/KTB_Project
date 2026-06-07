package ktb.ayden.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenInformationDto {
    //발급된 토큰과 그 만료 정보를 함께 전달
    private String accessToken;
    private long expiresIn;
}
