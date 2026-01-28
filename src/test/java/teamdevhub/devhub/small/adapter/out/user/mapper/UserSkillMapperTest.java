package teamdevhub.devhub.small.adapter.out.user.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.core.user.domain.vo.skill.UserSkill;
import teamdevhub.devhub.infrastructure.user.adapter.out.entity.UserSkillEntity;
import teamdevhub.devhub.infrastructure.user.adapter.out.mapper.UserSkillMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserSkillMapperTest {

    @Test
    @DisplayName("레코드에서_엔티티로_전환한다")
    void toEntity_convertsRecordToEntityCorrectly() {
        // given
        UserSkill userSkill = new UserSkill(TEST_USER_GUID_1, TEST_SKILL_CD);

        // when
        UserSkillEntity userSkillEntity = UserSkillMapper.toEntity(TEST_USER_SKILL_GUID, userSkill);

        // then
        assertThat(userSkillEntity.getUserSkillGuid()).isEqualTo(TEST_USER_SKILL_GUID);
        assertThat(userSkillEntity.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(userSkillEntity.getSkillCd()).isEqualTo(TEST_SKILL_CD);
    }

    @Test
    @DisplayName("엔티티에서_레코드로_전환한다")
    void toRecord_convertsEntityToRecordCorrectly() {
        // given
        UserSkillEntity userSkillEntity = UserSkillEntity.builder()
                .userSkillGuid(TEST_USER_SKILL_GUID)
                .userGuid(TEST_USER_GUID_1)
                .skillCd(TEST_SKILL_CD)
                .build();

        // when
        UserSkill userSkill = UserSkillMapper.toRecord(userSkillEntity);

        // then
        assertThat(userSkill.userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(userSkill.skillCd()).isEqualTo(TEST_SKILL_CD);
    }

}