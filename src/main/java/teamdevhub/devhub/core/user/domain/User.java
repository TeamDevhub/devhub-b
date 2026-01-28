package teamdevhub.devhub.core.user.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.core.common.audit.AuditInfo;
import teamdevhub.devhub.core.common.exception.DomainRuleException;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.user.domain.vo.position.UserPosition;
import teamdevhub.devhub.core.user.domain.vo.position.UserPositionChangeResult;
import teamdevhub.devhub.core.user.domain.vo.skill.UserSkill;
import teamdevhub.devhub.core.user.domain.vo.skill.UserSkillChangeResult;
import teamdevhub.devhub.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.core.user.domain.vo.command.UpdateUserCommand;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
public class User {

    private final String userGuid;
    private VerificationProvider verificationProvider;
    private String oauthId;

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
    private LocalDateTime lastLoginDate;

    private final AuditInfo auditInfo;

    @Builder
    private User(
            String userGuid,
            VerificationProvider verificationProvider,
            String oauthId,
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
            LocalDateTime lastLoginDate,
            AuditInfo auditInfo
    ) {
        validate(email, password);

        this.userGuid = userGuid;
        this.verificationProvider = verificationProvider;
        this.oauthId = oauthId;

        this.email = email;
        this.password = password;
        this.userRole = userRole;

        this.username = username;
        this.introduction = introduction;

        this.positions = Objects.requireNonNullElseGet(positions, HashSet::new);
        this.skills = Objects.requireNonNullElseGet(skills, HashSet::new);

        this.mannerDegree = mannerDegree;

        this.blocked = blocked;
        this.blockEndDate = blockEndDate;
        this.deleted = deleted;
        this.lastLoginDate = lastLoginDate;

        if (auditInfo == null) {
            this.auditInfo = AuditInfo.empty();
        } else {
            this.auditInfo = auditInfo;
        }
    }

    public static User createAdminUser(CreateUserCommand adminCreateUserCommand) {
        return User.builder()
                .userGuid(adminCreateUserCommand.userGuid())
                .verificationProvider(VerificationProvider.EMAIL)
                .oauthId(adminCreateUserCommand.email())
                .email(adminCreateUserCommand.email())
                .password(adminCreateUserCommand.encodedPassword())
                .userRole(UserRole.ADMIN)
                .username(adminCreateUserCommand.username())
                .blocked(false)
                .deleted(false)
                .auditInfo(AuditInfo.empty())
                .build();
    }

    public static User createGeneralUser(CreateUserCommand generalCreateUserCommand) {
        return User.builder()
                .userGuid(generalCreateUserCommand.userGuid())
                .verificationProvider(VerificationProvider.EMAIL)
                .oauthId(generalCreateUserCommand.email())
                .email(generalCreateUserCommand.email())
                .password(generalCreateUserCommand.encodedPassword())
                .userRole(UserRole.USER)
                .username(generalCreateUserCommand.username())
                .introduction(generalCreateUserCommand.introduction())
                .mannerDegree(36.5)
                .blocked(false)
                .deleted(false)
                .auditInfo(AuditInfo.empty())
                .build();
    }

    public static User createOauthUser(CreateUserCommand oauthCreateUserCommand) {
        return User.builder()
                .userGuid(oauthCreateUserCommand.userGuid())
                .verificationProvider(oauthCreateUserCommand.verificationProvider())
                .oauthId(oauthCreateUserCommand.oauthId())
                .email(oauthCreateUserCommand.email())
                .password(oauthCreateUserCommand.encodedPassword())
                .username(oauthCreateUserCommand.username())
                .introduction(oauthCreateUserCommand.introduction())
                .userRole(UserRole.USER)
                .mannerDegree(36.5)
                .blocked(false)
                .deleted(false)
                .auditInfo(AuditInfo.empty())
                .build();
    }

    public static User of(
            String userGuid,
            VerificationProvider verificationProvider,
            String oauthId,
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
                .verificationProvider(verificationProvider)
                .oauthId(oauthId)
                .email(email)
                .password(password)
                .userRole(userRole)
                .username(username)
                .introduction(introduction)
                .mannerDegree(mannerDegree)
                .blocked(blocked)
                .blockEndDate(blockEndDate)
                .deleted(deleted)
                .lastLoginDate(lastLoginDateTime)
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

    public void updateBasicProfile(UpdateUserCommand updateUserCommand) {
        if (hasText(updateUserCommand.username()) && !updateUserCommand.username().equals(this.username)) {
            this.username = updateUserCommand.username();
        }

        if (hasText(updateUserCommand.introduction()) && !updateUserCommand.introduction().equals(this.introduction)) {
            this.introduction = updateUserCommand.introduction();
        }
    }

    public UserPositionChangeResult changePositions(Set<UserPosition> newPositions) {
        if (newPositions == null || newPositions.isEmpty()) {
            return UserPositionChangeResult.unchanged(this.positions);
        }

        boolean hasNullPositionCd = newPositions.stream()
                .anyMatch(userPosition -> userPosition.positionCd() == null || userPosition.positionCd().isBlank());

        if (hasNullPositionCd) {
            return UserPositionChangeResult.unchanged(this.positions);
        }

        if (!this.positions.equals(newPositions)) {
            Set<UserPosition> oldPositions = Set.copyOf(positions);
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

        boolean hasNullSkillCd = newSkills.stream()
                .anyMatch(userSkill -> userSkill.skillCd() == null || userSkill.skillCd().isBlank());

        if (hasNullSkillCd) {
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

    public void loadPositionsAndSkills(Set<UserPosition> positions, Set<UserSkill> skills) {
        this.positions = new HashSet<>(positions);
        this.skills = new HashSet<>(skills);
    }

    private void validate(String email, String password) {
        if (!hasText(email)) {
            throw DomainRuleException.of(ErrorCode.USER_ID_FAIL);
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}