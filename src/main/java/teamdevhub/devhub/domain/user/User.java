package teamdevhub.devhub.domain.user;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.exception.DomainRuleException;
import teamdevhub.devhub.domain.user.vo.UserPosition;
import teamdevhub.devhub.domain.user.vo.UserPositionChangeResult;
import teamdevhub.devhub.domain.user.vo.UserSkill;
import teamdevhub.devhub.domain.user.vo.UserSkillChangeResult;
import teamdevhub.devhub.domain.vo.audit.AuditInfo;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
public class User {

    private final String userGuid;

    private final String email;
    private String password;
    private final UserRole userRole;

    private String username;
    private String introduction;

    private Set<UserPosition> positions;
    private Set<UserSkill> skills;

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
            Set<UserPosition> positions,
            Set<UserSkill> skills,
            double mannerDegree,
            boolean blocked,
            LocalDateTime blockEndDate,
            boolean deleted,
            LocalDateTime lastLoginDateTime,
            AuditInfo auditInfo
    ) {
        validate(email, password);

        this.userGuid = userGuid;
        this.email = email;
        this.password = password;
        this.username = username;
        this.userRole = userRole;
        this.introduction = introduction;

        this.positions = Objects.requireNonNullElseGet(positions, HashSet::new);
        this.skills = Objects.requireNonNullElseGet(skills, HashSet::new);

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

    public static User createGeneralUser(
            String userGuid,
            String email,
            String password,
            String username,
            String introduction
    ) {

        return User.builder()
                .userGuid(userGuid)
                .email(email)
                .password(password)
                .username(username)
                .userRole(UserRole.USER)
                .introduction(introduction)
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
            throw DomainRuleException.of(ErrorCode.ALREADY_DELETED);
        }
        this.deleted = true;
        this.blocked = false;
    }

    public void updateUsernameAndIntroduction(String newUsername, String newIntroduction) {
        if (hasText(newUsername) && !newUsername.equals(this.username)) {
            this.username = newUsername;
        }

        if (hasText(newIntroduction) && !newIntroduction.equals(this.introduction)) {
            this.introduction = newIntroduction;
        }
    }

    public UserPositionChangeResult changePositions(Set<UserPosition> newPositions) {
        if (newPositions == null || newPositions.isEmpty()) {
            return UserPositionChangeResult.unchanged(this.positions);
        }

        if (!this.positions.equals(newPositions)) {
            Set<UserPosition> oldPositions = Set.copyOf(this.positions);
            this.positions.clear();
            this.positions.addAll(newPositions);
            return UserPositionChangeResult.changed(oldPositions, this.positions);
        }

        return UserPositionChangeResult.unchanged(this.positions);
    }

    public UserSkillChangeResult changeSkills(Set<UserSkill> newSkills) {
        if (newSkills == null || newSkills.isEmpty()) {
            return UserSkillChangeResult.unchanged(this.skills);
        }

        if (!this.skills.equals(newSkills)) {
            Set<UserSkill> oldSkills = Set.copyOf(this.skills);
            this.skills.clear();
            this.skills.addAll(newSkills);
            return UserSkillChangeResult.changed(oldSkills, this.skills);
        }

        return UserSkillChangeResult.unchanged(this.skills);
    }

    private void validate(String email, String password) {
        if (!hasText(email)) {
            throw DomainRuleException.of(ErrorCode.USER_ID_FAIL);
        }

        if (!hasText(password) || password.length() < 8) {
            throw DomainRuleException.of(ErrorCode.USER_PASSWORD_FAIL);
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    public void loadPositionsAndSkills(Set<UserPosition> positions, Set<UserSkill> skills) {
        this.positions = positions;
        this.skills = skills;
    }
}