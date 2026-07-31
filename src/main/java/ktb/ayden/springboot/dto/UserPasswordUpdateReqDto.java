package ktb.ayden.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserPasswordUpdateReqDto {
    @NotBlank(message = "기존 비밀번호를 입력해주세요")
    private String currentPassword;
    @NotBlank(message = "새 비밀번호를 입력해주세요")
    @Size(min = 8, max=20, message = "비밀번호는 8자이상 20자 이하")
    private String newPassword;
    @NotBlank(message = "새 비밀번호를 한 번 더 입력해주세요")
    private String newPasswordCheck;

}
