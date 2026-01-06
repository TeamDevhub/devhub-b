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

    private String createdBy;
    private LocalDateTime createdAt;
    private String modifiedBy;
    private LocalDateTime modifiedAt;

    public static SignupResponseDto fromDomain(User user) {
        return SignupResponseDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .createdBy(user.getAuditInfo().createdBy())
                .createdAt(user.getAuditInfo().createdAt())
                .modifiedBy(user.getAuditInfo().modifiedBy())
                .modifiedAt(user.getAuditInfo().modifiedAt())
                .build();
    }
}