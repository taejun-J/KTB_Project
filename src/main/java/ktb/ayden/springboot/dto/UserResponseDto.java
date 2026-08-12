package ktb.ayden.springboot.dto;

import ktb.ayden.springboot.common.EntityStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ktb.ayden.springboot.entity.User;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String nickName;
    private String profileImage;
    private EntityStatus status;
    private LocalDateTime createdAt;

    //응답DTO에 엔티티의 값을 넣어 응답을 위한 객체 생성
    //이거 없으면 응답 DTO는 깡통
    public UserResponseDto(User user){
        this.userId = user.getUserId();
        this.nickName = user.getNickName();
        this.profileImage = user.getProfileImage();
        this.status = user.getStatus();
        this.createdAt = user.getCreatedAt();
    }
}
