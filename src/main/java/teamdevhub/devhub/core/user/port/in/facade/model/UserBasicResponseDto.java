package teamdevhub.devhub.core.user.port.in.facade.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import teamdevhub.devhub.core.user.domain.User;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
public class UserBasicResponseDto {

    private String userGuid;
    private String email;

    private String username;
    private String introduction;

    private String fileGuid;

    private double mannerDegree;

    private boolean blocked;
    private LocalDateTime blockEndDate;

    private boolean deleted;

    private LocalDateTime lastLoginDateTime;

    private String registrantGuid;
    private LocalDateTime registeredDate;
    private String modifierGuid;
    private LocalDateTime modifiedDate;

    public static UserBasicResponseDto fromDomain(User user) {

        return UserBasicResponseDto.builder()
                .userGuid(user.getUserGuid())
                .email(user.getEmail())
                .username(user.getUsername())
                .introduction(user.getIntroduction())
                .fileGuid(user.getFileGuid())
                .mannerDegree(user.getMannerDegree())
                .blocked(user.isBlocked())
                .blockEndDate(user.getBlockEndDate())
                .deleted(user.isDeleted())
                .lastLoginDateTime(user.getLastLoginDate())
                .registrantGuid(user.getAuditInfo().registrantGuid())
                .registeredDate(user.getAuditInfo().registeredDate())
                .modifierGuid(user.getAuditInfo().modifierGuid())
                .modifiedDate(user.getAuditInfo().modifiedDate())
                .build();
    }
}