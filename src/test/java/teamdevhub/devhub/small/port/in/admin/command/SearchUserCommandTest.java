package teamdevhub.devhub.small.port.in.admin.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;
import teamdevhub.devhub.adapter.in.dto.request.user.SearchUserRequestDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SearchUserCommandTest {

    @Test
    @DisplayName("blocked_가_Y_이면_true_로_변환된다")
    void returnTrueWhenBlockedIsY() {
        // given
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .blocked("Y")
                .build();

        // when
        SearchUserCommand searchUserCommand = SearchUserCommand.fromSearchUserRequestDto(searchUserRequestDto);

        // then
        assertThat(searchUserCommand.getBlocked()).isTrue();
    }

    @Test
    @DisplayName("blocked_가_N_이면_false_로_변환된다")
    void returnFalseWhenBlockedIsN() {
        // given
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .blocked("N")
                .build();

        // when
        SearchUserCommand searchUserCommand = SearchUserCommand.fromSearchUserRequestDto(searchUserRequestDto);

        // then
        assertThat(searchUserCommand.getBlocked()).isFalse();
    }

    @Test
    @DisplayName("blocked_가_Y_이면_true_로_변환된다")
    void returnNullWhenBlockedIsNull() {
        // given
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .blocked(null)
                .build();

        // when
        SearchUserCommand searchUserCommand = SearchUserCommand.fromSearchUserRequestDto(searchUserRequestDto);

        // then
        assertThat(searchUserCommand.getBlocked()).isNull();
    }

    @Test
    @DisplayName("keyword_가_blank_이면_null_로_변환된다")
    void returnNullWhenKeywordIsBlank() {
        // given
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .keyword("   ")
                .build();

        // when
        SearchUserCommand searchUserCommand = SearchUserCommand.fromSearchUserRequestDto(searchUserRequestDto);

        // then
        assertThat(searchUserCommand.getKeyword()).isNull();
    }

    @Test
    @DisplayName("keyword_가_존재하면_trim_되어_설정된다")
    void trimKeywordWhenItExists() {
        // given
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .keyword("  hello  ")
                .build();

        // when
        SearchUserCommand searchUserCommand = SearchUserCommand.fromSearchUserRequestDto(searchUserRequestDto);

        // then
        assertThat(searchUserCommand.getKeyword()).isEqualTo("hello");
    }

    @Test
    @DisplayName("joinedFrom_과_joinedTo_는_그대로_전달된다")
    void preserveJoinedFromAndJoinedToValues() {
        // given
        LocalDateTime joinedFrom = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime joinedTo = LocalDateTime.of(2024, 12, 31, 23, 59);

        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .joinedFrom(joinedFrom)
                .joinedTo(joinedTo)
                .build();

        // when
        SearchUserCommand searchUserCommand = SearchUserCommand.fromSearchUserRequestDto(searchUserRequestDto);

        // then
        assertThat(searchUserCommand.getJoinedFrom()).isEqualTo(joinedFrom);
        assertThat(searchUserCommand.getJoinedTo()).isEqualTo(joinedTo);
    }

    @Test
    @DisplayName("blocked_는_대소문자_구분없이_동작한다")
    void shouldHandleBlockedCaseInsensitively() {
        // given
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .blocked("y")
                .build();

        // when
        SearchUserCommand searchUserCommand = SearchUserCommand.fromSearchUserRequestDto(searchUserRequestDto);

        // then
        assertThat(searchUserCommand.getBlocked()).isTrue();
    }
}