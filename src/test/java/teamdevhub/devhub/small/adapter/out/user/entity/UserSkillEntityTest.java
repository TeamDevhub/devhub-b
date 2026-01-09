package teamdevhub.devhub.small.adapter.out.user.entity;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.user.entity.UserSkillEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.small.constant.TestConstant.*;

class UserSkillEntityTest {

    @Test
    void UserSkillEntity_를_생성하고_getter_를_사용할_수_있다() {
        // given
        UserSkillEntity userSkillEntity = UserSkillEntity.builder()
                .userSkillGuid(USER_SKILL_GUID)
                .userGuid(TEST_GUID)
                .skillCd(SKILL_CD)
                .build();

        // then
        assertThat(userSkillEntity.getUserSkillGuid()).isEqualTo(USER_SKILL_GUID);
        assertThat(userSkillEntity.getUserGuid()).isEqualTo(TEST_GUID);
        assertThat(userSkillEntity.getSkillCd()).isEqualTo(SKILL_CD);
    }
}