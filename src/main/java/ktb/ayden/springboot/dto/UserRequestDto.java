package ktb.ayden.springboot.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
//dto에서도 기본 생성자가 필요
//회원가입용으로 사용
@NoArgsConstructor
public class UserRequestDto {
    @NotBlank(message="이메일은 필수입니다.")
    //이메일 형식 확인을 위해 추가
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    //피그마 설계 반영
    @Size(min = 8, max=20, message = "비밀번호는 8자이상 20자 이하")
    private String password;
    @NotBlank(message = "비밀번호를 한 번더 입력해주세요")
    private String passwordCheck;
    @NotBlank(message="닉네임을 입력해주세요")
    @Size(max=10,message = "닉네임은 최대 10글자입니다.")
    private String nickName;
    private String profileImage;
}
