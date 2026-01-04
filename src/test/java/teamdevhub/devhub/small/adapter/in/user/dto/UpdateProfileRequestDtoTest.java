package teamdevhub.devhub.small.adapter.in.user.dto;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.user.dto.request.UpdateProfileRequestDto;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class UpdateProfileRequestDtoTest {

    private static final String NEW_USERNAME = "NewUsername";
    private static final String NEW_INTRO = "NewIntro";
    private static final List<String> NEW_POSITION_LIST = List.of("002");
    private static final List<String> NEW_SKILL_LIST = List.of("002");

    @Test
    void 변경된_값은_UpdateProfileRequestDto_로_변환된다() {
        //given, when
        UpdateProfileRequestDto updateProfileRequestDto = UpdateProfileRequestDto.builder()
                .username(NEW_USERNAME)
                .introduction(NEW_INTRO)
                .positionList(NEW_POSITION_LIST)
                .skillList(NEW_SKILL_LIST)
                .build();

        //then
        assertThat(updateProfileRequestDto.getUsername()).isEqualTo(NEW_USERNAME);
    }
}