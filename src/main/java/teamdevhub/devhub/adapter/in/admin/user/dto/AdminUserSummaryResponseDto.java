package teamdevhub.devhub.adapter.in.admin.user.dto;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;

import java.time.LocalDateTime;

@Getter
@Builder
public class AdminUserSummaryResponseDto {

    private String userGuid;
    private String email;
    private String password;
    private UserRole userRole;

    private String username;
    private String introduction;

    private double mannerDegree;

    private boolean blocked;
    private LocalDateTime blockEndDate;
    private boolean deleted;
    private LocalDateTime lastLoginDateTime;

    private String createdBy;
    private LocalDateTime createdAt;
    private String modifiedBy;
    private LocalDateTime modifiedAt;

    public static AdminUserSummaryResponseDto fromDomain(User user) {
        return AdminUserSummaryResponseDto.builder()
                .userGuid(user.getUserGuid())
                .email(user.getEmail())
                .username(user.getUsername())
                .introduction(user.getIntroduction())
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
