package teamdevhub.devhub.small.adapter.out.user.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.user.entity.UserSkillEntity;
import teamdevhub.devhub.adapter.out.user.mapper.UserSkillMapper;
import teamdevhub.devhub.domain.user.vo.UserSkill;

import static org.assertj.core.api.Assertions.assertThat;

class UserSkillMapperTest {

    @Test
    @DisplayName("레코드에서_엔티티로_전환한다")
    void toEntity_convertsRecordToEntityCorrectly() {
        // given
        UserSkill record = new UserSkill("user-1", "SKILL_001");
        String guid = "skill-guid-123";

        // when
        UserSkillEntity entity = UserSkillMapper.toEntity(guid, record);

        // then
        assertThat(entity.getUserSkillGuid()).isEqualTo(guid);
        assertThat(entity.getUserGuid()).isEqualTo("user-1");
        assertThat(entity.getSkillCd()).isEqualTo("SKILL_001");
    }

    @Test
    @DisplayName("엔티티에서_레코드로_전환한다")
    void toRecord_convertsEntityToRecordCorrectly() {
        // given
        UserSkillEntity entity = UserSkillEntity.builder()
                .userSkillGuid("skill-guid-123")
                .userGuid("user-1")
                .skillCd("SKILL_001")
                .build();

        // when
        UserSkill record = UserSkillMapper.toRecord(entity);

        // then
        assertThat(record.userGuid()).isEqualTo("user-1");
        assertThat(record.skillCd()).isEqualTo("SKILL_001");
    }

}