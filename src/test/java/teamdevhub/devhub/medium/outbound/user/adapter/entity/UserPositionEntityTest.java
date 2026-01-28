package teamdevhub.devhub.medium.outbound.user.adapter.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.outbound.user.adapter.entity.UserPositionEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserPositionEntityTest {

    @Test
    @DisplayName("UserPositionEntity_를_생성하고_getter_를_사용할_수_있다")
    void createEntityAndUseGetter() {
        // given, when
        UserPositionEntity userPositionEntity = UserPositionEntity.builder()
                .userPositionGuid(TEST_USER_POSITION_GUID)
                .userGuid(TEST_USER_GUID_1)
                .positionCd(TEST_POSITION_CD)
                .build();

        // then
        assertThat(userPositionEntity.getUserPositionGuid()).isEqualTo(TEST_USER_POSITION_GUID);
        assertThat(userPositionEntity.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(userPositionEntity.getPositionCd()).isEqualTo(TEST_POSITION_CD);
    }
}