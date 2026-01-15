package teamdevhub.devhub.small.adapter.in.user.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.dto.request.user.UpdateProfileRequestDto;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UpdateProfileRequestDtoTest {

    @Test
    @DisplayName("변경된_값은_UpdateProfileRequestDto_로_변환된다")
    void convertUpdatedValuesToRequestDto() {
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