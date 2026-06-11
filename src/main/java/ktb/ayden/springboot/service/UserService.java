package ktb.ayden.springboot.service;

import jakarta.transaction.Transactional;

import ktb.ayden.springboot.common.entityStatus;
import ktb.ayden.springboot.dto.*;
import ktb.ayden.springboot.entity.RefreshToken;
import ktb.ayden.springboot.entity.User;
import ktb.ayden.springboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
//인증,인가 이후
import ktb.ayden.springboot.auth.JwtProvider;
import ktb.ayden.springboot.repository.RefreshTokenRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    //@RequiredArgsConstructor가 생성자 자동생성 (final 붙어야함)
    private final UserRepository userRepository;
    //인증,인가 이후
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    //1. 회원가입
    //이 메서드 안의 DB작업을 하나의 묶음으로 처리하라는 것 == 원자성
    @Transactional
    //UserSignupResponseDto를 반환하는 CreateUser메소드 선언
    public UserResponseDto createUser(UserRequestDto request){
        User user = new User(
                request.getEmail(),
                request.getPassword(),
                request.getNickname(),
                request.getProfileImage()
        );
        User savedUser = userRepository.save(user);
        //엔티티 -> 응답DTO(필요한 값만 컨트롤러에 전달)
        return new UserResponseDto(savedUser);
    }
    //2. 유저 조회
    @Transactional
    public UserResponseDto getUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("요청한 대상을 찾을 수 없습니다. 주소가 정확한지 다시 한 번 확인해주세요."));
        return new UserResponseDto(user);
    }
    //3. 회원정보 수정(프로필이미지, 닉네임)
    @Transactional
    public UserResponseDto updateUser(Long userId, UserRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("요청한 대상을 찾을 수 없습니다. 주소가 정확한지 다시 한 번 확인해주세요."));
        user.changeUserInformation(
                request.getProfileImage(),
                request.getNickname()
        );
        return new UserResponseDto(user);
    }
    //4. 회원정보 수정(비밀번호)
    @Transactional
    public UserResponseDto updateUserPassword(Long userId, UserRequestDto request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("요청한 대상을 찾을 수 없습니다. 주소가 정확한지 다시 한 번 확인해주세요."));
        user.changeUserPassword(request.getPassword());
        return new UserResponseDto(user);
    }

    //5. 회원탈퇴(소프트딜리트)
    @Transactional
    public UserResponseDto softDeleteUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("요청한 대상을 찾을 수 없습니다. 주소가 정확한지 다시 한 번 확인해주세요."));
        user.changeUserStatus(entityStatus.INACTIVE);
        return new UserResponseDto(user);
    }

    //인증,인가 이후 로그인 관련
    // 로그인
    @Transactional
    public LoginResultDto login(LoginRequestDto loginRequest) {

        // 1. 로그인 요청으로 들어온 email로 사용자 조회
        // 로그인할 때 사용자는 userId가 아니라 email/password를 입력하므로 email로 찾음
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

        // 2. 비밀번호 검증
        // 지금은 평문 비교 -> 비밀번호 암호화 비교로 바꿔야 함
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        // 3. Access Token 생성
        // Access Token은 API 요청할 때 Authorization 헤더에 넣어서 사용
        String accessToken = jwtProvider.createAccessToken(
                user.getUserId(),
                user.getEmail(),
                user.getNickName()
        );

        // 4. Refresh Token 생성
        // Refresh Token은 Access Token 재발급에 사용
        String refreshToken = jwtProvider.createRefreshToken(user.getUserId());

        // 5. 기존 Refresh Token 삭제
        // 한 사용자당 Refresh Token을 하나만 유지하려는 구조
        refreshTokenRepository.deleteByUserId(user.getUserId());

        // 6. 새 Refresh Token DB 저장
        refreshTokenRepository.save(
                new RefreshToken(
                        refreshToken,
                        user.getUserId(),
                        LocalDateTime.now().plusDays(14)
                )
        );

        // 7. 컨트롤러로 결과 반환
        // response에는 클라이언트에게 줄 Access Token 정보
        // refreshToken은 컨트롤러에서 쿠키에 담기 위해 같이 반환
        return new LoginResultDto(
                new LoginResponseDto(
                        new UserResponseDto(user),
                        new TokenInformationDto(
                                accessToken,
                                jwtProvider.getAccessTokenValidityInMilliseconds()
                        )
                ),
                refreshToken
        );
    }
        // Access Token 재발급
        @Transactional
        public TokenResultDto refreshAccessToken(String refreshToken) {

            // 1. 쿠키에서 refreshToken이 안 넘어온 경우
            if (refreshToken == null) {
                throw new IllegalArgumentException("Refresh Token이 없습니다.");
            }

            // 2. DB에서 Refresh Token 조회
            RefreshToken saved = refreshTokenRepository.findByToken(refreshToken)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Refresh Token입니다."));

            // 3. Refresh Token 만료 여부 확인
            if (saved.isExpired()) {
                refreshTokenRepository.delete(saved);
                throw new IllegalArgumentException("만료된 Refresh Token입니다.");
            }

            // 4. Refresh Token에 저장된 userId로 사용자 조회
            User user = userRepository.findById(saved.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

            // 5. 새 Access Token 생성
            String newAccessToken = jwtProvider.createAccessToken(
                    user.getUserId(),
                    user.getEmail(),
                    user.getNickName()
            );

            // 6. Refresh Token 회전
            // 기존 Refresh Token을 삭제하고 새 Refresh Token을 다시 발급
            String newRefreshToken = jwtProvider.createRefreshToken(user.getUserId());

            refreshTokenRepository.delete(saved);

            refreshTokenRepository.save(
                    new RefreshToken(
                            newRefreshToken,
                            user.getUserId(),
                            LocalDateTime.now().plusDays(14)
                    )
            );

            // 7. 새 Access Token 정보 + 새 Refresh Token 반환
            return new TokenResultDto(
                    new TokenInformationDto(
                            newAccessToken,
                            jwtProvider.getAccessTokenValidityInMilliseconds()
                    ),
                    newRefreshToken
            );
        }
}
