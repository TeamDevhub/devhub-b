package teamdevhub.devhub.small.adapter.in.admin.dto;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.admin.user.dto.SearchUserRequestDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SearchUserRequestDtoTest {

    @Test
    void builder_로_모든_필드를_생성할_수_있다() {
        // given
        LocalDateTime joinedFrom = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime joinedTo = LocalDateTime.of(2024, 12, 31, 23, 59);

        // when
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .blocked("Y")
                .joinedFrom(joinedFrom)
                .joinedTo(joinedTo)
                .keyword("test")
                .build();

        // then
        assertThat(searchUserRequestDto.getBlocked()).isEqualTo("Y");
        assertThat(searchUserRequestDto.getJoinedFrom()).isEqualTo(joinedFrom);
        assertThat(searchUserRequestDto.getJoinedTo()).isEqualTo(joinedTo);
        assertThat(searchUserRequestDto.getKeyword()).isEqualTo("test");
    }

    @Test
    void 모든_필드는_null_이어도_생성된다() {
        // given, when
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder().build();

        // then
        assertThat(searchUserRequestDto.getBlocked()).isNull();
        assertThat(searchUserRequestDto.getJoinedFrom()).isNull();
        assertThat(searchUserRequestDto.getJoinedTo()).isNull();
        assertThat(searchUserRequestDto.getKeyword()).isNull();
    }
}