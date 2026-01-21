package teamdevhub.devhub.small.adapter.in.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.user.dto.request.UpdateProfileRequestDto;
import teamdevhub.devhub.domain.user.vo.position.UserPosition;
import teamdevhub.devhub.domain.user.vo.skill.UserSkill;
import teamdevhub.devhub.port.in.user.command.UpdateProfileCommand;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UpdateProfileRequestDtoTest {

    @Test
    @DisplayName("모든_값이_존재하면_UpdateProfileCommand_로_변환된다")
    void convertAllValuesToCommand() {
        // given
        UpdateProfileRequestDto updateProfileRequestDto = UpdateProfileRequestDto.builder()
                .username(NEW_USERNAME)
                .introduction(NEW_INTRO)
                .positionList(NEW_POSITION_LIST)
                .skillList(NEW_SKILL_LIST)
                .build();

        // when
        UpdateProfileCommand updateProfileCommand = updateProfileRequestDto.toUpdateProfileCommand(TEST_USER_GUID_1);

        // then
        assertThat(updateProfileCommand.username()).isEqualTo(NEW_USERNAME);
        assertThat(updateProfileCommand.introduction()).isEqualTo(NEW_INTRO);
        assertThat(updateProfileCommand.positions()).hasSize(NEW_POSITION_LIST.size())
                .extracting(UserPosition::positionCd)
                .containsExactlyInAnyOrderElementsOf(NEW_POSITION_LIST);
        assertThat(updateProfileCommand.skills()).hasSize(NEW_SKILL_LIST.size())
                .extracting(UserSkill::skillCd)
                .containsExactlyInAnyOrderElementsOf(NEW_SKILL_LIST);
    }

    @Test
    @DisplayName("positionList_가_null_이면_positions_는_null_이다")
    void positionsIsNullWhenPositionListIsNull() {
        // given
        UpdateProfileRequestDto updateProfileRequestDto = UpdateProfileRequestDto.builder()
                .username(NEW_USERNAME)
                .positionList(null)
                .build();

        // when
        UpdateProfileCommand updateProfileCommand = updateProfileRequestDto.toUpdateProfileCommand(TEST_USER_GUID_1);

        // then
        assertThat(updateProfileCommand.positions()).isNull();
    }

    @Test
    @DisplayName("skillList_가_null_이면_skills_는_null_이다")
    void skillsIsNullWhenSkillListIsNull() {
        // given
        UpdateProfileRequestDto updateProfileRequestDto = UpdateProfileRequestDto.builder()
                .introduction(NEW_INTRO)
                .skillList(null)
                .build();

        // when
        UpdateProfileCommand updateProfileCommand = updateProfileRequestDto.toUpdateProfileCommand(TEST_USER_GUID_1);

        // then
        assertThat(updateProfileCommand.skills()).isNull();
    }

    @Test
    @DisplayName("positionList_가_빈값이면_positions_는_빈_집합으로_변환된다")
    void positionsIsEmptyWhenPositionListIsEmpty() {
        // given
        UpdateProfileRequestDto updateProfileRequestDto = UpdateProfileRequestDto.builder()
                .positionList(List.of())
                .build();

        // when
        UpdateProfileCommand updateProfileCommand = updateProfileRequestDto.toUpdateProfileCommand(TEST_USER_GUID_1);

        // then
        assertThat(updateProfileCommand.positions()).isEmpty();
    }

    @Test
    @DisplayName("skillList_가_빈값이면_skills_는_빈_집합으로_변환된다")
    void skillsIsEmptyWhenSkillListIsEmpty() {
        // given
        UpdateProfileRequestDto updateProfileRequestDto = UpdateProfileRequestDto.builder()
                .skillList(List.of())
                .build();

        // when
        UpdateProfileCommand updateProfileCommand = updateProfileRequestDto.toUpdateProfileCommand(TEST_USER_GUID_1);

        // then
        assertThat(updateProfileCommand.skills()).isEmpty();
    }
}