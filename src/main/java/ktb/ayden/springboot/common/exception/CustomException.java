package ktb.ayden.springboot.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode){
        //에러 메시지 받아오기
        super(errorCode.getMessage());
        //에러 코드(상태) 받기
        this.errorCode = errorCode;
    }

}
