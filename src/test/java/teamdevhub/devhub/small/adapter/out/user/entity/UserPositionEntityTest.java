package teamdevhub.devhub.small.adapter.out.user.entity;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.user.entity.UserPositionEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.small.common.mock.constant.TestConstant.*;

class UserPositionEntityTest {

    @Test
    void UserPositionEntity__를_생성하고_getter_를_사용할_수_있다() {
        // given, when
        UserPositionEntity userPositionEntity = UserPositionEntity.builder()
                .userInterestPositionGuid(TEST_POSITION_GUID)
                .userGuid(TEST_GUID)
                .positionCd(TEST_POSITION_CD)
                .build();

        // then
        assertThat(userPositionEntity.getUserInterestPositionGuid()).isEqualTo(TEST_POSITION_GUID);
        assertThat(userPositionEntity.getUserGuid()).isEqualTo(TEST_GUID);
        assertThat(userPositionEntity.getPositionCd()).isEqualTo(TEST_POSITION_CD);
    }
}