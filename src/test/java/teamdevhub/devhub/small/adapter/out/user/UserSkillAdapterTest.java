package teamdevhub.devhub.small.adapter.out.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.user.UserSkillAdapter;
import teamdevhub.devhub.common.provider.uuid.IdentifierProvider;
import teamdevhub.devhub.domain.user.vo.UserSkill;
import teamdevhub.devhub.fake.pure.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.fake.spring.persistence.user.FakeJpaUserSkillRepository;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_USER_GUID_1;

public class UserSkillAdapterTest {

    private UserSkillAdapter userSkillAdapter;

    @BeforeEach
    void init() {
        FakeJpaUserSkillRepository jpaUserSkillRepository = new FakeJpaUserSkillRepository();
        IdentifierProvider identifierProvider = new FakeUuidIdentifierProvider(TEST_USER_GUID_1);
        userSkillAdapter = new UserSkillAdapter(jpaUserSkillRepository, identifierProvider);
    }

    @Test
    @DisplayName("전체_스킬을_저장한다")
    void saveAll_savesSkillsCorrectly() {
        // given
        UserSkill skill1 = new UserSkill("user-1", "001");
        UserSkill skill2 = new UserSkill("user-1", "002");
        Set<UserSkill> skills = new HashSet<>(Set.of(skill1, skill2));

        // when
        userSkillAdapter.saveAll(skills);

        // then
        Set<UserSkill> saved = userSkillAdapter.findByUserGuid("user-1");
        assertThat(saved).hasSize(2)
                .extracting(UserSkill::skillCd)
                .containsExactlyInAnyOrder("001", "002");
    }

    @Test
    @DisplayName("스킬이_변경되면_모든_변경사항이_반영된다")
    void replace_mergesOldAndNewSkillsCorrectly() {
        // given
        UserSkill oldSkill = new UserSkill("user-1", "001");
        Set<UserSkill> previousSkills = new HashSet<>(Set.of(oldSkill));
        userSkillAdapter.saveAll(previousSkills);

        UserSkill newSkill = new UserSkill("user-1", "002");
        Set<UserSkill> currentSkills = new HashSet<>(Set.of(oldSkill, newSkill));

        // when
        userSkillAdapter.replace(previousSkills, currentSkills);

        // then
        Set<UserSkill> finalSkills = userSkillAdapter.findByUserGuid("user-1");
        assertThat(finalSkills).hasSize(2)
                .extracting(UserSkill::skillCd)
                .containsExactlyInAnyOrder("001", "002");
    }

    @Test
    @DisplayName("삭제될_스킬값은_제거된다")
    void replace_removesDeletedSkillsCorrectly() {
        // given
        UserSkill oldSkill1 = new UserSkill("user-1", "001");
        UserSkill oldSkill2 = new UserSkill("user-1", "002");
        Set<UserSkill> previousSkills = new HashSet<>(Set.of(oldSkill1, oldSkill2));
        userSkillAdapter.saveAll(previousSkills);

        Set<UserSkill> currentSkills = new HashSet<>(Set.of(oldSkill1));

        // when
        userSkillAdapter.replace(previousSkills, currentSkills);

        // then
        Set<UserSkill> finalSkills = userSkillAdapter.findByUserGuid("user-1");
        assertThat(finalSkills).hasSize(1)
                .extracting(UserSkill::skillCd)
                .containsExactly("001");
    }

    @Test
    @DisplayName("변경사항이_없으면_스킬은_그대로 유지된다")
    void replace_noChanges_doesNothing() {
        // given
        UserSkill skill = new UserSkill("user-1", "001");
        Set<UserSkill> skills = new HashSet<>(Set.of(skill));
        userSkillAdapter.saveAll(skills);

        // when
        userSkillAdapter.replace(skills, skills);

        // then
        Set<UserSkill> finalSkills = userSkillAdapter.findByUserGuid("user-1");
        assertThat(finalSkills).hasSize(1)
                .extracting(UserSkill::skillCd)
                .containsExactly("001");
    }
}
