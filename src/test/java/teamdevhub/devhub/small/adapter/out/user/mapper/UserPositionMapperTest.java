package teamdevhub.devhub.small.adapter.out.user.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.user.entity.UserPositionEntity;
import teamdevhub.devhub.adapter.out.user.mapper.UserPositionMapper;
import teamdevhub.devhub.domain.user.vo.UserPosition;

import static org.assertj.core.api.Assertions.assertThat;

class UserPositionMapperTest {

    @Test
    @DisplayName("레코드에서_엔티티로_전환한다")
    void toEntity_convertsRecordToEntityCorrectly() {
        // given
        UserPosition record = new UserPosition("user-1", "001");
        String guid = "pos-guid-123";

        // when
        UserPositionEntity entity = UserPositionMapper.toEntity(guid, record);

        // then
        assertThat(entity.getUserPositionGuid()).isEqualTo(guid);
        assertThat(entity.getUserGuid()).isEqualTo("user-1");
        assertThat(entity.getPositionCd()).isEqualTo("001");
    }

    @Test
    @DisplayName("엔티티에서_레코드로_전환한다")
    void toRecord_convertsEntityToRecordCorrectly() {
        // given
        UserPositionEntity entity = UserPositionEntity.builder()
                .userPositionGuid("pos-guid-123")
                .userGuid("user-1")
                .positionCd("001")
                .build();

        // when
        UserPosition record = UserPositionMapper.toRecord(entity);

        // then
        assertThat(record.userGuid()).isEqualTo("user-1");
        assertThat(record.positionCd()).isEqualTo("001");
    }

}