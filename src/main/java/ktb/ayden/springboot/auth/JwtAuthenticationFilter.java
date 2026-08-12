package ktb.ayden.springboot.auth;

import org.springframework.lang.NonNull;
import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
//요청 단위로 실행 여부 제어해서 한 번만 적용하게 하는 것 (OncePerrequestFilter)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
//화이트 리스트는 로그인 안 해도 접근이 가능한 API목록
    private static final String[] WHITE_LIST = {
            "/users/auth",
            "/users/token/refresh",
            "/actuator/health",
            "/users/email/check",
            "/users/nickname/check",
            "/users/profile-image"
    };

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        //OPTIONS요청시 -> 필터 적용안하고 통과
        //브라우저가 CORS확인하려고 자동으로 보내는 사전요청이 OPTIONS
        String uri = request.getRequestURI();
        String method = request.getMethod();

        // OPTIONS 요청은 CORS 사전 요청이므로 JWT 검사하지 않음
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }
        //User관련
        //회원가입 검사X
        if (uri.equals("/users") && "POST".equalsIgnoreCase(method)) {
            return true;
        }
        //로그인 검사X
        if (uri.equals("/users/auth") && "POST".equalsIgnoreCase(method)) {
            return true;
        }
        //토큰 재발급 검사X
        if (uri.equals("/users/token/refresh") && "POST".equalsIgnoreCase(method)) {
            return true;
        }
        //Post관련
        //게시글 목록 조회 검사X
        //필터를 아예 건너뛰면 userId 속성이 안 들어가서 상세 조회에서 좋아요 여부(isLiked)를 알 수 없음
        //-> 스킵 대신 "선택적 인증"으로 전환 (isOptionalAuth 참고)
//        if (uri.startsWith("/posts") && "GET".equalsIgnoreCase(method)) {
//            return true;
//        }
        // Actuator 헬스체크 검사 X
        if (uri.equals("/actuator/health") && "GET".equalsIgnoreCase(method)) {
            return true;
        }
        if (uri.equals("/users/email/check") && "GET".equalsIgnoreCase(method)) {
            return true;
        }
        if (uri.equals("/users/nickname/check") && "GET".equalsIgnoreCase(method)) {
            return true;
        }
        if (uri.equals("/users/profile-image") && "GET".equalsIgnoreCase(method)) {
            return true;
        }

        return false;

    }

    //선택적 인증 대상 = 비로그인도 접근 가능하지만, 토큰이 있으면 userId를 꺼내두는 요청
    //게시글/댓글 조회가 여기에 해당 (로그인 상태면 isLiked 같은 개인화 정보를 내려주기 위함)
    private boolean isOptionalAuth(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/posts")
                && "GET".equalsIgnoreCase(request.getMethod());
    }

    @Override
    //Authorization 헤더 확인
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        //뒤에 붙은 AUTHORIZATION 이런 것은 HTTP표준에 정의된 상수 -> 오타 방지에 좋음(request.getHeader("Authorization")과 같음)
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 토큰이 없거나 형식이 틀리면 401
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // 선택적 인증 요청은 토큰 없이도 통과 (userId 미설정)
            if (isOptionalAuth(request)) {
                filterChain.doFilter(request, response);
                return;
            }
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = authHeader.substring(7);

        // 기존에는 filterChain.doFilter가 try 안에 있어서 컨트롤러 예외까지 401로 덮였음
        // -> 토큰 검증만 try로 감싸고, 체인 호출은 밖으로 분리
//        try {
//            // 토큰 서명 + 만료 검증
//            jwtProvider.parse(token);
//
//            // access 토큰인지 확인
//            if (!jwtProvider.isAccessToken(token)) {
//                throw new IllegalArgumentException("Not access token");
//            }
//            //필터에서 꺼낸 userId를 Request에 담기
//            Long userId = jwtProvider.getUserId(token);
//            request.setAttribute("userId",userId);
//            filterChain.doFilter(request, response);
//
//        } catch (Exception exception) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        }
        try {
            // 토큰 서명 + 만료 검증
            jwtProvider.parse(token);

            // access 토큰인지 확인
            if (!jwtProvider.isAccessToken(token)) {
                throw new IllegalArgumentException("Not access token");
            }
            //필터에서 꺼낸 userId를 Request에 담기
            Long userId = jwtProvider.getUserId(token);
            request.setAttribute("userId", userId);

        } catch (Exception exception) {
            // 토큰이 깨졌어도 선택적 인증 요청이면 비로그인으로 간주하고 통과
            if (!isOptionalAuth(request)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
