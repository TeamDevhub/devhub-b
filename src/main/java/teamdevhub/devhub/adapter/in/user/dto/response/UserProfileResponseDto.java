package teamdevhub.devhub.adapter.in.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class UserProfileResponseDto {

    private String userGuid;
    private String email;
    private String password;
    private UserRole userRole;

    private String username;
    private String introduction;

    private List<String> positionList;
    private List<String> skillList;

    private double mannerDegree;

    private boolean blocked;
    private LocalDateTime blockEndDate;
    private boolean deleted;
    private LocalDateTime lastLoginDateTime;

    private String createdBy;
    private LocalDateTime createdAt;
    private String modifiedBy;
    private LocalDateTime modifiedAt;

    public static UserProfileResponseDto fromDomain(User user) {
        return UserProfileResponseDto.builder()
                .userGuid(user.getUserGuid())
                .email(user.getEmail())
                .username(user.getUsername())
                .introduction(user.getIntroduction())
                .positionList(user.getPositionList())
                .skillList(user.getSkillList())
                .mannerDegree(user.getMannerDegree())
                .blocked(user.isBlocked())
                .blockEndDate(user.getBlockEndDate())
                .deleted(user.isDeleted())
                .lastLoginDateTime(user.getLastLoginDateTime())
                .createdBy(user.getAuditInfo().getCreatedBy())
                .createdAt(user.getAuditInfo().getCreatedAt())
                .modifiedBy(user.getAuditInfo().getModifiedBy())
                .modifiedAt(user.getAuditInfo().getModifiedAt())
                .build();
    }
}
