package teamdevhub.devhub.adapter.out.user;

import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.domain.common.AuditInfo;
import teamdevhub.devhub.domain.user.User;

public class UserMapper {

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .userGuid(user.getUserGuid())
                .email(user.getEmail())
                .password(user.getPassword())
                .userRole(user.getUserRole())
                .username(user.getUsername())
                .introduction(user.getIntroduction())
                .mannerDegree(user.getMannerDegree())
                .blocked(user.isBlocked())
                .blockEndDate(user.getBlockEndDate())
                .deleted(user.isDeleted())
                .lastLoginDt(user.getLastLoginDateTime())
                .build();
    }

    public static User toDomain(UserEntity userEntity) {
        return User.of(
                userEntity.getUserGuid(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getUserRole(),
                userEntity.getUsername(),
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