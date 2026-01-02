package teamdevhub.devhub.adapter.in.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.domain.user.User;

import java.time.LocalDateTime;

@Getter
@Builder
public class SignupResponseDto {

    private String email;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String createdBy;
    private String modifiedBy;

    public static SignupResponseDto fromUserDomain(User user) {
        return SignupResponseDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .createdAt(user.getAuditInfo().getCreatedAt())
                .modifiedAt(user.getAuditInfo().getModifiedAt())
                .createdBy(user.getAuditInfo().getCreatedBy())
                .modifiedBy(user.getAuditInfo().getModifiedBy())
                .build();
    }
}