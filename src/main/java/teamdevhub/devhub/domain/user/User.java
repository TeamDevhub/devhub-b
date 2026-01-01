package teamdevhub.devhub.domain.user;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.exception.DomainRuleException;
import teamdevhub.devhub.domain.common.AuditInfo;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class User {

    private final String userGuid;

    private final String email;
    private String password;
    private final UserRole userRole;

    private String username;
    private String introduction;
    private double mannerDegree;

    private boolean blocked;
    private LocalDateTime blockEndDate;
    private boolean deleted;
    private LocalDateTime lastLoginDateTime;

    private final AuditInfo auditInfo;

    @Builder
    private User(
            String userGuid,
            String email,
            String password,
            UserRole userRole,
            String username,
            String introduction,
            List<String> skillList,
            double mannerDegree,
            boolean blocked,
            LocalDateTime blockEndDate,
            boolean deleted,
            LocalDateTime lastLoginDateTime,
            AuditInfo auditInfo
    ) {
        if (email == null || email.isBlank()) {
            throw DomainRuleException.of(ErrorCodeEnum.USER_ID_FAIL);
        }
        if (password == null || password.length() < 6) {
            throw DomainRuleException.of(ErrorCodeEnum.USER_PASSWORD_FAIL);
        }

        this.userGuid = userGuid;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.username = username;
        this.introduction = introduction;
        this.mannerDegree = mannerDegree;
        this.blocked = blocked;
        this.blockEndDate = blockEndDate;
        this.deleted = deleted;
        this.lastLoginDateTime = lastLoginDateTime;
        this.auditInfo = auditInfo != null ? auditInfo : AuditInfo.empty(); //refactor
    }

    public static User createGeneralUser(String userGuid, String email, String username, String password, String introduction) {
        return User.builder()
                .userGuid(userGuid)
                .email(email)
                .username(username)
                .password(password)
                .userRole(UserRole.USER)
                .introduction(introduction)
                .blocked(false)
                .deleted(false)
                .mannerDegree(36.5)
                .auditInfo(AuditInfo.empty())
                .build();
    }

    public static User createAdminUser(String userGuid, String email, String username, String password) {
        return User.builder()
                .userGuid(userGuid)
                .email(email)
                .username(username)
                .password(password)
                .userRole(UserRole.ADMIN)
                .blocked(false)
                .deleted(false)
                .auditInfo(AuditInfo.empty())
                .build();
    }

    public static User of(
            String userGuid,
            String email,
            String password,
            UserRole userRole,
            String username,
            String introduction,
            double mannerDegree,
            boolean blocked,
            LocalDateTime blockEndDate,
            boolean deleted,
            LocalDateTime lastLoginDateTime,
            AuditInfo auditInfo
    ) {
        return User.builder()
                .userGuid(userGuid)
                .email(email)
                .password(password)
                .userRole(userRole)
                .username(username)
                .introduction(introduction)
                .mannerDegree(mannerDegree)
                .blocked(blocked)
                .blockEndDate(blockEndDate)
                .deleted(deleted)
                .lastLoginDateTime(lastLoginDateTime)
                .auditInfo(auditInfo)
                .build();
    }

    public void login(LocalDateTime loginDateTime) {
        this.lastLoginDateTime = loginDateTime;
    }

    public boolean hasRole(UserRole checkRole) {
        return this.userRole == checkRole;
    }

    public void changeUsername(String newUsername) {
        if (newUsername == null || newUsername.isBlank()) {
            throw DomainRuleException.of(ErrorCodeEnum.UNKNOWN_FAIL);
        }
        this.username = newUsername;
    }

    public void blockUntil(LocalDateTime endDate) {
        this.blocked = true;
        this.blockEndDate = endDate;
    }

    public void unblock() {
        this.blocked = false;
        this.blockEndDate = null;
    }
}