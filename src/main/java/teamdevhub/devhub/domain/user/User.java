package teamdevhub.devhub.domain.user;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.exception.DomainRuleException;
import teamdevhub.devhub.domain.common.AuditInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class User {

    private final String userGuid;

    private final String email;
    private String password;
    private final UserRole userRole;

    private String username;
    private String introduction;

    private final List<String> positionList;
    private final List<String> skillList;

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
            String username,
            UserRole userRole,
            String introduction,
            List<String> positionList,
            List<String> skillList,
            double mannerDegree,
            boolean blocked,
            LocalDateTime blockEndDate,
            boolean deleted,
            LocalDateTime lastLoginDateTime,
            AuditInfo auditInfo
    ) {
        validate(email, password, userRole, positionList, skillList);

        this.userGuid = userGuid;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.username = username;
        this.introduction = introduction;

        this.positionList = new ArrayList<>();
        if (positionList != null) {
            this.positionList.addAll(positionList);
        }

        this.skillList = new ArrayList<>();
        if (skillList != null) {
            this.skillList.addAll(skillList);
        }

        this.mannerDegree = mannerDegree;
        this.blocked = blocked;
        this.blockEndDate = blockEndDate;
        this.deleted = deleted;
        this.lastLoginDateTime = lastLoginDateTime;

        if (auditInfo == null) {
            this.auditInfo = AuditInfo.empty();
        } else {
            this.auditInfo = auditInfo;
        }
    }

    private void validate(
            String email,
            String password,
            UserRole userRole,
            List<String> positionList,
            List<String> skillList
    ) {
        if (!hasText(email)) {
            throw DomainRuleException.of(ErrorCodeEnum.USER_ID_FAIL);
        }

        if (!hasText(password) || password.length() < 6) {
            throw DomainRuleException.of(ErrorCodeEnum.USER_PASSWORD_FAIL);
        }

        if (userRole != UserRole.USER) {
            return;
        }

        if (positionList == null || positionList.isEmpty()) {
            throw DomainRuleException.of(ErrorCodeEnum.USER_POSITION_REQUIRED);
        }

        if (skillList == null || skillList.isEmpty()) {
            throw DomainRuleException.of(ErrorCodeEnum.USER_SKILL_REQUIRED);
        }
    }

    public static User createGeneralUser(
            String userGuid,
            String email,
            String password,
            String username,
            String introduction,
            List<String> positionList,
            List<String> skillList
    ) {
        return User.builder()
                .userGuid(userGuid)
                .email(email)
                .password(password)
                .username(username)
                .userRole(UserRole.USER)
                .introduction(introduction)
                .positionList(positionList)
                .skillList(skillList)
                .mannerDegree(36.5)
                .blocked(false)
                .deleted(false)
                .auditInfo(AuditInfo.empty())
                .build();
    }

    public static User createAdminUser(
            String userGuid,
            String email,
            String password,
            String username
    ) {
        return User.builder()
                .userGuid(userGuid)
                .email(email)
                .password(password)
                .username(username)
                .userRole(UserRole.ADMIN)
                .positionList(List.of())
                .skillList(List.of())
                .blocked(false)
                .deleted(false)
                .auditInfo(AuditInfo.empty())
                .build();
    }

    public static User of(
            String userGuid,
            String email,
            String password,
            String username,
            UserRole userRole,
            String introduction,
            List<String> positionList,
            List<String> skillList,
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
                .username(username)
                .userRole(userRole)
                .introduction(introduction)
                .positionList(positionList)
                .skillList(skillList)
                .mannerDegree(mannerDegree)
                .blocked(blocked)
                .blockEndDate(blockEndDate)
                .deleted(deleted)
                .lastLoginDateTime(lastLoginDateTime)
                .auditInfo(auditInfo)
                .build();
    }

    public void withdraw() {
        if (this.deleted) {
            throw DomainRuleException.of(ErrorCodeEnum.ALREADY_DELETED);
        }
        this.deleted = true;
        this.blocked = false;
    }

    public void updateProfile(String username, String introduction) {
        if (hasText(username)) {
            this.username = username;
        }
        if (hasText(introduction)) {
            this.introduction = introduction;
        }
    }

    public void updatePositionList(List<String> newPositionList) {
        if (newPositionList == null || newPositionList.isEmpty()) {
            throw DomainRuleException.of(ErrorCodeEnum.USER_POSITION_REQUIRED);
        }
        this.positionList.clear();
        this.positionList.addAll(newPositionList);
    }

    public void updateSkillList(List<String> newSkillList) {
        if (newSkillList == null || newSkillList.isEmpty()) {
            throw DomainRuleException.of(ErrorCodeEnum.USER_SKILL_REQUIRED);
        }
        this.skillList.clear();
        this.skillList.addAll(newSkillList);
    }

    public boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    public boolean hasUserRole(UserRole checkRole) {
        return this.userRole == checkRole;
    }

    public void blockUntil(LocalDateTime blockEndDate) {
        this.blocked = true;
        this.blockEndDate = blockEndDate;
    }
}