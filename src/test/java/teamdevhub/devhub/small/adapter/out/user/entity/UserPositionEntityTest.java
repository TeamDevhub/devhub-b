package teamdevhub.devhub.small.adapter.out.user.entity;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.user.entity.UserPositionEntity;

import static org.assertj.core.api.Assertions.assertThat;

class UserPositionEntityTest {

    private static final String TEST_GUID = "USERa1b2c3d4e5f6g7h8i9j10k11l12m";
    private static final String POSITION_CD = "001";
    private static final String USER_POSITION_GUID = "POSa1b2c3d4e5f6g7h8i9j10k11l12";

    @Test
    void UserPositionEntity__를_생성하고_getter_를_사용할_수_있다() {
        //given, when
        UserPositionEntity userPositionEntity = UserPositionEntity.builder()
                .userInterestPositionGuid(USER_POSITION_GUID)
                .userGuid(TEST_GUID)
                .positionCd(POSITION_CD)
                .build();

        //then
        assertThat(userPositionEntity.getUserInterestPositionGuid()).isEqualTo(USER_POSITION_GUID);
        assertThat(userPositionEntity.getUserGuid()).isEqualTo(TEST_GUID);
        assertThat(userPositionEntity.getPositionCd()).isEqualTo(POSITION_CD);
    }
}