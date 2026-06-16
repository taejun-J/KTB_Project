package ktb.ayden.springboot.controller;

import jakarta.validation.Valid;
import ktb.ayden.springboot.dto.*;
import ktb.ayden.springboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//인증인가이후
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

//HTTP요청을 받는 컨트롤러를 의미 <- RestController
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    //회원가입
    @PostMapping
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto request){
       //서비스 호출
        return userService.createUser(request);
    }
    //회원조회
    @GetMapping("/{userId}")
    public UserResponseDto getUser(@PathVariable Long userId){
        return userService.getUser(userId);
    }
    //회원 정보 수정(프로필 사진, 닉네임)
    //현재 로그인 한 유저의 정보도 받아오도록 수정 -> 나중에 수정,삭제에서의 검증을 위해
    @PatchMapping("/{userId}")
    public UserResponseDto updateUser(@RequestAttribute("userId") Long loginUserId,@PathVariable Long userId, @Valid @RequestBody UserUpdateRequestDto request){
        return userService.updateUser(loginUserId,userId,request);
    }
    //회원 정보 수정 (비밀번호)
    @PutMapping("/{userId}/password")
    public UserResponseDto updateUserPassword(@RequestAttribute("userId") Long loginUserId, @PathVariable Long userId,@Valid @RequestBody UserPasswordUpdateReqDto request){
        return userService.updateUserPassword(loginUserId,userId,request);
    }
    //회원탈퇴
    @DeleteMapping("/{userId}")
    public UserResponseDto softDeleteUser(@RequestAttribute("userId") Long loginUserId, @PathVariable Long userId){
        return userService.softDeleteUser(loginUserId, userId);
    }

    //하단은 로그인 관련(인증,인가 추가 이후)

    // 로그인
    @PostMapping("/auth")
    public LoginResponseDto login(
            @Valid @RequestBody LoginRequestDto loginRequest,
            HttpServletResponse httpResponse
    ) {

        // 이메일 + 비밀번호 검증
        // Access Token + Refresh Token 생성
        LoginResultDto result = userService.login(loginRequest);

        // Refresh Token은 쿠키에 저장
        ResponseCookie refreshCookie = ResponseCookie
                .from("refreshToken", result.getRefreshToken())
                .httpOnly(true)      // JS 접근 불가
                .secure(false)       // HTTPS 환경이면 true 권장
                .path("/")
                .maxAge(14 * 24 * 60 * 60) // 14일
                .sameSite("Strict")
                .build();

        // Set-Cookie 헤더 추가
        httpResponse.addHeader(
                HttpHeaders.SET_COOKIE,
                refreshCookie.toString()
        );

        // Access Token 정보 반환
        return result.getResponse();
    }

    // 액세스 토큰 재발급
    // ===================== Access Token 재발급 =====================
    @PostMapping("/token/refresh")
    public TokenInformationDto refreshAccessToken(
            // 브라우저 쿠키에 저장된 Refresh Token 읽기
            @CookieValue(
                    name = "refreshToken",
                    required = false
            ) String refreshToken,

            HttpServletResponse httpResponse
    ) {

        // Refresh Token 검증
        // 새로운 Access Token 생성
        // (필요하면 Refresh Token도 재발급)
        TokenResultDto result =
                userService.refreshAccessToken(refreshToken);

        // Refresh Token 회전(Rotation)
        // 새 Refresh Token이 발급된 경우
        if (result.getNewRefreshToken() != null) {

            ResponseCookie cookie = ResponseCookie
                    .from(
                            "refreshToken",
                            result.getNewRefreshToken()
                    )
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(14 * 24 * 60 * 60)
                    .sameSite("Lax")
                    .build();

            httpResponse.addHeader(
                    HttpHeaders.SET_COOKIE,
                    cookie.toString()
            );
        }

        // 새 Access Token 반환
        return result.getToken();
    }

}
