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

    private String registrantGuid;
    private LocalDateTime registeredDate;
    private String modifierGuid;
    private LocalDateTime modifiedDate;

    public static SignupResponseDto fromDomain(User user) {
        return SignupResponseDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .registrantGuid(user.getAuditInfo().registrantGuid())
                .registeredDate(user.getAuditInfo().registeredDate())
                .modifierGuid(user.getAuditInfo().modifierGuid())
                .modifiedDate(user.getAuditInfo().modifiedDate())
                .build();
    }
}