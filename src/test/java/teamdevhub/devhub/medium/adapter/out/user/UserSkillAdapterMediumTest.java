package teamdevhub.devhub.medium.adapter.out.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.adapter.out.user.UserSkillAdapter;
import teamdevhub.devhub.adapter.out.infrastructure.persistence.user.JpaUserSkillRepository;
import teamdevhub.devhub.domain.user.vo.UserSkill;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

@SpringBootTest
@Transactional
class UserSkillAdapterMediumTest {

    @Autowired
    private UserSkillAdapter userSkillAdapter;

    @Autowired
    private JpaUserSkillRepository jpaUserSkillRepository;

    @BeforeEach
    void init() {
        jpaUserSkillRepository.deleteAll();
    }

    @Test
    @DisplayName("전체 스킬을 저장한다")
    void saveAll_savesSkillsCorrectly() {
        // given
        UserSkill skill1 = new UserSkill(TEST_USER_GUID_1, TEST_SKILL_CD);
        UserSkill skill2 = new UserSkill(TEST_USER_GUID_1, NEW_SKILL_CD);
        Set<UserSkill> skills = new HashSet<>(Set.of(skill1, skill2));

        // when
        userSkillAdapter.saveAll(skills);

        // then
        Set<UserSkill> saved = userSkillAdapter.findByUserGuid(TEST_USER_GUID_1);

        assertThat(saved).hasSize(2)
                .extracting(UserSkill::skillCd)
                .containsExactlyInAnyOrder(TEST_SKILL_CD, NEW_SKILL_CD);
    }

    @Test
    @DisplayName("스킬이 변경되면 모든 변경사항이 반영된다")
    void replace_mergesOldAndNewSkillsCorrectly() {
        // given
        UserSkill oldSkill = new UserSkill(TEST_USER_GUID_1, TEST_SKILL_CD);
        Set<UserSkill> previousSkills = new HashSet<>(Set.of(oldSkill));
        userSkillAdapter.saveAll(previousSkills);

        UserSkill newSkill = new UserSkill(TEST_USER_GUID_1, NEW_SKILL_CD);
        Set<UserSkill> currentSkills = new HashSet<>(Set.of(oldSkill, newSkill));

        // when
        userSkillAdapter.replace(previousSkills, currentSkills);

        // then
        Set<UserSkill> finalSkills = userSkillAdapter.findByUserGuid(TEST_USER_GUID_1);

        assertThat(finalSkills).hasSize(2)
                .extracting(UserSkill::skillCd)
                .containsExactlyInAnyOrder(TEST_SKILL_CD, NEW_SKILL_CD);
    }

    @Test
    @DisplayName("삭제될 스킬값은 제거된다")
    void replace_removesDeletedSkillsCorrectly() {
        // given
        UserSkill oldSkill1 = new UserSkill(TEST_USER_GUID_1, TEST_SKILL_CD);
        UserSkill oldSkill2 = new UserSkill(TEST_USER_GUID_1, NEW_SKILL_CD);
        Set<UserSkill> previousSkills = new HashSet<>(Set.of(oldSkill1, oldSkill2));
        userSkillAdapter.saveAll(previousSkills);

        Set<UserSkill> currentSkills = new HashSet<>(Set.of(oldSkill1));

        // when
        userSkillAdapter.replace(previousSkills, currentSkills);

        // then
        Set<UserSkill> finalSkills = userSkillAdapter.findByUserGuid(TEST_USER_GUID_1);

        assertThat(finalSkills).hasSize(1)
                .extracting(UserSkill::skillCd)
                .containsExactly(TEST_SKILL_CD);
    }

    @Test
    @DisplayName("변경사항이 없으면 스킬은 그대로 유지된다")
    void replace_noChanges_doesNothing() {
        // given
        UserSkill skill = new UserSkill(TEST_USER_GUID_1, TEST_SKILL_CD);
        Set<UserSkill> skills = new HashSet<>(Set.of(skill));
        userSkillAdapter.saveAll(skills);

        // when
        userSkillAdapter.replace(skills, skills);

        // then
        Set<UserSkill> finalSkills = userSkillAdapter.findByUserGuid(TEST_USER_GUID_1);

        assertThat(finalSkills).hasSize(1)
                .extracting(UserSkill::skillCd)
                .containsExactly(TEST_SKILL_CD);
    }
}