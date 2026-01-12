package teamdevhub.devhub.adapter.out.user.mapper;

import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.vo.audit.AuditInfo;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;

public class UserMapper {

    public static AuthenticatedUser toAuthenticatedUser(UserEntity userEntity) {
        return AuthenticatedUser.of(userEntity.getUserGuid(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getUserRole()
        );
    }

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .userGuid(user.getUserGuid())
                .email(user.getEmail())
                .password(user.getPassword())
                .username(user.getUsername())
                .userRole(user.getUserRole())
                .introduction(user.getIntroduction())
                .mannerDegree(user.getMannerDegree())
                .blocked(user.isBlocked())
                .blockEndDate(user.getBlockEndDate())
                .deleted(user.isDeleted())
                .lastLoginDt(user.getLastLoginDateTime())
                .build();
    }

    public static User toDomain(
            UserEntity userEntity
    ) {
        return User.of(
                userEntity.getUserGuid(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getUsername(),
                userEntity.getUserRole(),
                userEntity.getIntroduction(),
                userEntity.getMannerDegree(),
                userEntity.isBlocked(),
                userEntity.getBlockEndDate(),
                userEntity.isDeleted(),
                userEntity.getLastLoginDt(),
                toAuditInfo(userEntity)
        );
    }

    private static AuditInfo toAuditInfo(UserEntity userEntity) {
        return AuditInfo.of(
                userEntity.getRgtrId(),
                userEntity.getRegDt(),
                userEntity.getMdfrId(),
                userEntity.getMdfcnDt()
        );
    }
}