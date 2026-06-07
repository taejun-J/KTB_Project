package ktb.ayden.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResultDto {
    //응답 바디 (반환할 데이터만)
    private LoginResponseDto response;
    //쿠키(응답 바디에 포함X)
    private String refreshToken;
}
