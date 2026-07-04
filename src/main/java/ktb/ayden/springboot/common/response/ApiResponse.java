package ktb.ayden.springboot.common.response;

import lombok.Getter;

@Getter
//제너릭 사용 data값에는 다양한 DTO의 타입이 들어감 ->
public class ApiResponse<T> {
    //api Response success, message, data 이렇게 표시
    private final boolean success;
    private final String message;
    private final T data;

    private ApiResponse(boolean success, String message, T data){
        this.success = success;
        this.message = message;
        this.data = data;
    }
    // <T>-> 제너릭 T사용 선언, ApiResponse<T> -> 반환타입
    //하단은 data가 있는(반환시)버전
    public static<T> ApiResponse<T> success(T data, String message){
        return new ApiResponse<>(true, message, data);
    }
    //반환 data가 없는 버전
    public static ApiResponse<Void> success(String message){
        return new ApiResponse<>(true, message, null);
    }
}
