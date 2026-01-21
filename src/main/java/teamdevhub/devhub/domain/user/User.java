package teamdevhub.devhub.domain.user;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.domain.exception.DomainRuleException;
import teamdevhub.devhub.domain.user.vo.position.UserPosition;
import teamdevhub.devhub.domain.user.vo.position.UserPositionChangeResult;
import teamdevhub.devhub.domain.user.vo.skill.UserSkill;
import teamdevhub.devhub.domain.user.vo.skill.UserSkillChangeResult;
import teamdevhub.devhub.domain.common.vo.AuditInfo;
import teamdevhub.devhub.domain.user.vo.user.UserCreateCommand;
import teamdevhub.devhub.domain.user.vo.user.UserUpdateCommand;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
public class User {

    private final String userGuid;
    private SignupStatus signupStatus;
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
            SignupStatus signupStatus,
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
        this.signupStatus = signupStatus;
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

    public static User createAdminUser(UserCreateCommand adminUserCreateCommand) {
        return User.builder()
                .userGuid(adminUserCreateCommand.userGuid())
                .signupStatus(SignupStatus.COMPLETED)
                .verificationProvider(VerificationProvider.EMAIL)
                .oauthId(adminUserCreateCommand.email())
                .email(adminUserCreateCommand.email())
                .password(adminUserCreateCommand.encodedPassword())
                .userRole(UserRole.ADMIN)
                .username(adminUserCreateCommand.username())
                .blocked(false)
                .deleted(false)
                .auditInfo(AuditInfo.empty())
                .build();
    }

    public static User createGeneralUser(UserCreateCommand generalUserCreateCommand) {
        return User.builder()
                .userGuid(generalUserCreateCommand.userGuid())
                .signupStatus(SignupStatus.COMPLETED)
                .verificationProvider(VerificationProvider.EMAIL)
                .oauthId(generalUserCreateCommand.email())
                .email(generalUserCreateCommand.email())
                .password(generalUserCreateCommand.encodedPassword())
                .userRole(UserRole.USER)
                .username(generalUserCreateCommand.username())
                .introduction(generalUserCreateCommand.introduction())
                .mannerDegree(36.5)
                .blocked(false)
                .deleted(false)
                .auditInfo(AuditInfo.empty())
                .build();
    }

    public static User createOauthUser(UserCreateCommand oauthUserCreateCommand) {
        return User.builder()
                .userGuid(oauthUserCreateCommand.userGuid())
                .signupStatus(SignupStatus.PENDING)
                .verificationProvider(oauthUserCreateCommand.verificationProvider())
                .oauthId(oauthUserCreateCommand.oauthId())
                .email(oauthUserCreateCommand.email())
                .password(oauthUserCreateCommand.encodedPassword())
                .username(oauthUserCreateCommand.username())
                .introduction(oauthUserCreateCommand.introduction())
                .userRole(UserRole.USER)
                .mannerDegree(36.5)
                .blocked(false)
                .deleted(false)
                .auditInfo(AuditInfo.empty())
                .build();
    }

    public static User of(
            String userGuid,
            SignupStatus signupStatus,
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
                .signupStatus(signupStatus)
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

    public void completeSignup() {
        this.signupStatus = SignupStatus.COMPLETED;
    }

    public void withdraw() {
        if (this.deleted) {
            throw DomainRuleException.of(ErrorCode.ALREADY_DELETED);
        }
        this.deleted = true;
        this.blocked = false;
    }

    public void updateBasicProfile(UserUpdateCommand userUpdateCommand) {
        if (hasText(userUpdateCommand.username()) && !userUpdateCommand.username().equals(this.username)) {
            this.username = userUpdateCommand.username();
        }

        if (hasText(userUpdateCommand.introduction()) && !userUpdateCommand.introduction().equals(this.introduction)) {
            this.introduction = userUpdateCommand.introduction();
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