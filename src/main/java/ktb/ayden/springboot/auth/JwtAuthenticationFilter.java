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
            "/users/token/refresh"
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
        if (uri.startsWith("/posts") && "GET".equalsIgnoreCase(method)) {
            return true;
        }


        return false;

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
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = authHeader.substring(7);

        try {
            // 토큰 서명 + 만료 검증
            jwtProvider.parse(token);

            // access 토큰인지 확인
            if (!jwtProvider.isAccessToken(token)) {
                throw new IllegalArgumentException("Not access token");
            }
            //필터에서 꺼낸 userId를 Request에 담기
            Long userId = jwtProvider.getUserId(token);
            request.setAttribute("userId",userId);
            filterChain.doFilter(request, response);

        } catch (Exception exception) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
