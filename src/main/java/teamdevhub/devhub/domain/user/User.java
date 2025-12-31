package teamdevhub.devhub.domain.user;

import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.exception.DomainRuleException;
import teamdevhub.devhub.domain.common.AuditInfo;

import java.time.LocalDate;
import java.util.List;

public class User {

    private final String userGuid;

    private final String email;
    private final String password;

    private final String username;
//    private final String introduction;
//    private final List<String> skillList;
//    private final int mannerDegree;
//
//    private final String blockYn;
//    private final LocalDate blockEndDate;
//    private final LocalDate lastLoginDate;
//    private final String deleteYn;

    private final UserRole userRole;

    private final AuditInfo auditInfo;

    public User(String userGuid, String email, String username, String password, UserRole userRole, AuditInfo auditInfo) {
        if (email == null || email.isBlank()) {
            throw DomainRuleException.of(ErrorCodeEnum.USER_ID_FAIL);
        }
        if (password == null || password.length() < 6) {
            throw DomainRuleException.of(ErrorCodeEnum.USER_PASSWORD_FAIL);
        }

        this.userGuid = userGuid;
        this.email = email;
        this.username = username;
        this.password = password;
        this.userRole = userRole;
        this.auditInfo = auditInfo != null ? auditInfo : AuditInfo.empty();
    }

    public static User createGeneralUser(String userGuid, String email, String username, String password) {
        return new User(userGuid, email, username, password, UserRole.USER, AuditInfo.empty());
    }

    public static User createAdminUser(String userGuid, String email, String username, String password) {
        return new User(userGuid, email, username, password, UserRole.ADMIN, AuditInfo.empty());
    }

    public static User of(String userGuid, String email, String username, String password, UserRole role, AuditInfo auditInfo) {
        return new User(userGuid, email, username, password, role, auditInfo);
    }

    public boolean hasRole(UserRole checkRole) {
        return this.userRole == checkRole;
    }

    public User changeUsername(String newUsername) {
        return new User(this.userGuid, this.email, newUsername, this.password, this.userRole, this.auditInfo);
    }

    public String getUserGuid() { return userGuid; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public UserRole getUserRole() { return userRole; }
    public AuditInfo getAuditInfo() { return auditInfo; }
}