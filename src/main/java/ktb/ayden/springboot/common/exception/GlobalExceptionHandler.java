package ktb.ayden.springboot.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

//전역 처리임을 나타내기 위해 사용(어떤 컨트롤러든 발생하면 여기서 받음)
@RestControllerAdvice
public class GlobalExceptionHandler {

    //CustomException 발생 -> 이 하단 메서드 실행
    @ExceptionHandler(CustomException.class)
    //ResponseEntity = HTTP응답 전체를 다루는 객체
    //CustomException 발생시 -> HTTP응답 만들어 -> Map에 담아 보내겠다를 의미
    //handleCustomException -> 스프링이 자동으로 실행하는 것, 내가 따로 호출할 필요X
    public ResponseEntity<Map<String, Object>> handleCustomException(CustomException e) {
        //map은 K:V형태로 저장하기 위해 사용 (아래 처럼 쉼표로 넣어도)
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(Map.of(
                        "success", false,
                        "code", errorCode.name(),
                        "message", errorCode.getMessage()
                ));
    }
}