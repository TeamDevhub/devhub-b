package teamdevhub.devhub.adapter.in.admin.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
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

    public static AdminUserSummaryResponseDto fromEntity(UserEntity userEntity) {
        return AdminUserSummaryResponseDto.builder()
                .userGuid(userEntity.getUserGuid())
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .introduction(userEntity.getIntroduction())
                .mannerDegree(userEntity.getMannerDegree())
                .blocked(userEntity.isBlocked())
                .blockEndDate(userEntity.getBlockEndDate())
                .deleted(userEntity.isDeleted())
                .lastLoginDateTime(userEntity.getLastLoginDt())
                .createdBy(userEntity.getRgtrId())
                .createdAt(userEntity.getRegDt())
                .modifiedBy(userEntity.getMdfrId())
                .modifiedAt(userEntity.getMdfcnDt())
                .build();
    }
}
