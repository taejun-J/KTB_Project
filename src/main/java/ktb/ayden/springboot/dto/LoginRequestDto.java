package ktb.ayden.springboot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@Getter
@NoArgsConstructor
public class LoginRequestDto {
    @NotBlank(message = "REQUIRED")
    @Email(message = "INVALID_FORMAT")
    private String email;

    @NotBlank(message = "REQUIRED")
    @Size(min = 8, message = "TOO_SHORT")
    @Size(max = 20, message = "TOO_LONG")
    // 여기서 정규식 사용하여 유효성 검사 진행
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "INVALID_FORMAT"
    ) // 영문, 숫자, 특수문자를 최소 1개씩 포함해야 함
    private String password;
}
