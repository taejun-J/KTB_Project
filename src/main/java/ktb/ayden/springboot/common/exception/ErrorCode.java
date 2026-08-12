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
    ALREADY_LIKED_POST(HttpStatus.CONFLICT, "이미 좋아요를 누른 게시글입니다"),

    //s3용
    EMPTY_IMAGE(
            HttpStatus.BAD_REQUEST,
            "이미지 파일이 비어 있습니다."
    ),

    IMAGE_SIZE_EXCEEDED(
            HttpStatus.BAD_REQUEST,
            "이미지 크기는 5MB를 초과할 수 없습니다."
    ),

    INVALID_IMAGE_TYPE(
            HttpStatus.BAD_REQUEST,
            "JPEG, PNG, WEBP 이미지만 업로드할 수 있습니다."
    ),

    IMAGE_UPLOAD_FAILED(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "이미지 업로드에 실패했습니다."
    ),
    PROFILE_IMAGE_ALREADY_EXISTS(
            HttpStatus.CONFLICT,
            "프로필 이미지가 이미 등록되어 있습니다."
    );

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}