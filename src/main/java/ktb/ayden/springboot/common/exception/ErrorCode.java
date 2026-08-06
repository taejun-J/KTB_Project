package ktb.ayden.springboot.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),

    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),

    FORBIDDEN_USER(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    FORBIDDEN_POST(HttpStatus.FORBIDDEN, "게시글에 대한 권한이 없습니다."),

    EXIST_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),

    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호 확인이 일치하지 않습니다."),

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),

    POST_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND,"좋아요를 누른 적 없는 게시글입니다"),
    ALREADY_LIKED_POST(HttpStatus.CONFLICT, "이미 좋아요를 누른 게시글입니다");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}