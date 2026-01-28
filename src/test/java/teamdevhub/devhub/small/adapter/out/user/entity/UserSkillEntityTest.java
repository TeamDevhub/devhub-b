package teamdevhub.devhub.small.adapter.out.user.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.infrastructure.user.adapter.out.entity.UserSkillEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserSkillEntityTest {

    @Test
    @DisplayName("UserSkillEntity_를_생성하고_getter_를_사용할_수_있다")
    void createEntityAndUseGetter() {
        // given
        UserSkillEntity userSkillEntity = UserSkillEntity.builder()
                .userSkillGuid(TEST_USER_SKILL_GUID)
                .userGuid(TEST_USER_GUID_1)
                .skillCd(TEST_SKILL_CD)
                .build();

        // then
        assertThat(userSkillEntity.getUserSkillGuid()).isEqualTo(TEST_USER_SKILL_GUID);
        assertThat(userSkillEntity.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(userSkillEntity.getSkillCd()).isEqualTo(TEST_SKILL_CD);
    }
}