package teamdevhub.devhub.domain.user;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.exception.DomainRuleException;
import teamdevhub.devhub.domain.user.vo.UserPosition;
import teamdevhub.devhub.domain.user.vo.UserSkill;
import teamdevhub.devhub.domain.vo.audit.AuditInfo;

import java.time.LocalDateTime;
import java.util.HashSet;
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

        this.positions = new HashSet<>(positions);
        this.skills = new HashSet<>(skills);

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

    public void updateLastLoginDateTime(LocalDateTime now) {
        this.lastLoginDateTime = now;
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

    public void changePositionsAndSkills(
            Set<UserPosition> newPositions,
            Set<UserSkill> newSkills
    ) {

        if (newPositions == null && newSkills == null) {
            return;
        }

        if (newPositions == null || newPositions.isEmpty()) {
            throw DomainRuleException.of(ErrorCode.USER_POSITION_REQUIRED);
        }

        if (newSkills == null || newSkills.isEmpty()) {
            throw DomainRuleException.of(ErrorCode.USER_SKILL_REQUIRED);
        }

        if (!this.positions.equals(newPositions)) {
            this.positions.clear();
            this.positions.addAll(newPositions);
        }

        if (!this.skills.equals(newSkills)) {
            this.skills.clear();
            this.skills.addAll(newSkills);
        }
    }

    private void validate(
            String email,
            String password
    ) {
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

//    private static Set<UserPosition> toPositions(List<String> codes) {
//        if (codes == null) return Set.of();
//        return codes.stream()
//                .map(UserPosition::new)
//                .collect(Collectors.toUnmodifiableSet());
//    }
//
//    private static Set<UserSkill> toSkills(List<String> codes) {
//        if (codes == null) return Set.of();
//        return codes.stream()
//                .map(UserSkill::new)
//                .collect(Collectors.toUnmodifiableSet());
//    }
}