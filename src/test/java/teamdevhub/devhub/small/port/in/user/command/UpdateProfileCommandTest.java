package teamdevhub.devhub.small.port.in.user.command;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.port.in.user.command.UpdateProfileCommand;
import teamdevhub.devhub.adapter.in.user.dto.request.UpdateProfileRequestDto;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.small.mock.constant.TestConstant.*;

class UpdateProfileCommandTest {

    @Test
    void UpdateProfileRequestDto_를_UpdateProfileCommand_로_변환할_수_있다() {
        // given
        UpdateProfileRequestDto updateProfileRequestDto = UpdateProfileRequestDto.builder()
                .username(NEW_USERNAME)
                .introduction(NEW_INTRO)
                .positionList(NEW_POSITION_LIST)
                .skillList(NEW_SKILL_LIST)
                .build();

        // when
        UpdateProfileCommand updateProfileCommand = UpdateProfileCommand.fromUpdateProfileRequestDto(updateProfileRequestDto, TEST_GUID);

        // then
        assertThat(updateProfileCommand.getUserGuid()).isEqualTo(TEST_GUID);
        assertThat(updateProfileCommand.getUsername()).isEqualTo(NEW_USERNAME);
        assertThat(updateProfileCommand.getPositionList()).isEqualTo(NEW_POSITION_LIST);
    }
}