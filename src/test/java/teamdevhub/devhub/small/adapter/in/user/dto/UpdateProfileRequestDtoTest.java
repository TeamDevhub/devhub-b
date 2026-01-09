package teamdevhub.devhub.small.adapter.in.user.dto;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.user.dto.request.UpdateProfileRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateProfileRequestDtoTest {

    @Test
    void 변경된_값은_UpdateProfileRequestDto_로_변환된다() {
        // given, when
        UpdateProfileRequestDto updateProfileRequestDto = UpdateProfileRequestDto.builder()
                .username(NEW_USERNAME)
                .introduction(NEW_INTRO)
                .positionList(NEW_POSITION_LIST)
                .skillList(NEW_SKILL_LIST)
                .build();

        // then
        assertThat(updateProfileRequestDto.getUsername()).isEqualTo(NEW_USERNAME);
    }
}