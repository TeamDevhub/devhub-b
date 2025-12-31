package teamdevhub.devhub.adapter.in.user.dto;

import teamdevhub.devhub.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "회원가입 완료 정보")
public class SignupUserResponseDto {

    @Schema(description = "사용자 ID")
    private String email;

    @Schema(description = "사용자명")
    private String username;

    @Schema(description = "생성일")
    private LocalDateTime createdAt;

    @Schema(description = "수정일")
    private LocalDateTime modifiedAt;

    @Schema(description = "생성자")
    private String createdBy;

    @Schema(description = "수정자")
    private String modifiedBy;

    public static SignupUserResponseDto fromUserDomain(User user) {
        return SignupUserResponseDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .createdAt(user.getAuditInfo().getCreatedAt())
                .modifiedAt(user.getAuditInfo().getModifiedAt())
                .createdBy(user.getAuditInfo().getCreatedBy())
                .modifiedBy(user.getAuditInfo().getModifiedBy())
                .build();
    }
}