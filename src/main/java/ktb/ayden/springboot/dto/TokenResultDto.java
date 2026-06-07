package ktb.ayden.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResultDto {
    //응답 바디(accessToken, expiresIn)
    private TokenInformationDto token;
    //토큰 회전(RTR)이 발생된 경우에만 전달 아니면 null
    private String newRefreshToken;
}
