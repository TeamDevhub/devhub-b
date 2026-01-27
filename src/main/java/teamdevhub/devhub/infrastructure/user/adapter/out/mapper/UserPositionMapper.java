package teamdevhub.devhub.infrastructure.user.adapter.out.mapper;

import teamdevhub.devhub.infrastructure.user.adapter.out.entity.UserPositionEntity;
import teamdevhub.devhub.core.user.domain.vo.position.UserPosition;

public class UserPositionMapper {

    public static UserPositionEntity toEntity(String userPositionGuid, UserPosition userPosition) {
        return UserPositionEntity.builder()
                .userPositionGuid(userPositionGuid)
                .userGuid(userPosition.userGuid())
                .positionCd(userPosition.positionCd())
                .build();
    }

    public static UserPosition toRecord(UserPositionEntity userPositionEntity) {
        return new UserPosition(
                userPositionEntity.getUserGuid(),
                userPositionEntity.getPositionCd()
        );
    }
}
