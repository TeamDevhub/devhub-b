package teamdevhub.devhub.small.adapter.in.user.command;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.user.command.UpdateProfileCommand;
import teamdevhub.devhub.adapter.in.user.dto.request.UpdateProfileRequestDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateProfileCommandTest {

    private static final String TEST_GUID = "USERa1b2c3d4e5f6g7h8i9j10k11l12m";
    private static final String NEW_USERNAME = "NewUsername";
    private static final String NEW_INTRO = "NewIntro";
    private static final List<String> NEW_POSITION_LIST = List.of("002");
    private static final List<String> NEW_SKILL_LIST = List.of("002");


    @Test
    void UpdateProfileRequestDto_를_UpdateProfileCommand_로_변환할_수_있다() {
        //given
        UpdateProfileRequestDto updateProfileRequestDto = UpdateProfileRequestDto.builder()
                .username(NEW_USERNAME)
                .introduction(NEW_INTRO)
                .positionList(NEW_POSITION_LIST)
                .skillList(NEW_SKILL_LIST)
                .build();

        //when
        UpdateProfileCommand updateProfileCommand = UpdateProfileCommand.fromUpdateProfileRequestDto(updateProfileRequestDto, TEST_GUID);

        //then
        assertThat(updateProfileCommand.getUserGuid()).isEqualTo(TEST_GUID);
        assertThat(updateProfileCommand.getUsername()).isEqualTo(NEW_USERNAME);
        assertThat(updateProfileCommand.getPositionList()).isEqualTo(NEW_POSITION_LIST);
    }
}