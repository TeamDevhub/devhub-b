package teamdevhub.devhub.small.core.user.port.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.api.user.model.SearchUserRequestDto;
import teamdevhub.devhub.core.user.port.in.command.SearchUserCommand;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
        SearchUserCommand searchUserCommand = searchUserRequestDto.toSearchUserCommand();

        // then
        assertThat(searchUserCommand.blocked()).isTrue();
    }

    @Test
    @DisplayName("blocked_가_N_이면_false_로_변환된다")
    void returnFalseWhenBlockedIsN() {
        // given
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .blocked("N")
                .build();

        // when
        SearchUserCommand searchUserCommand = searchUserRequestDto.toSearchUserCommand();

        // then
        assertThat(searchUserCommand.blocked()).isFalse();
    }

    @Test
    @DisplayName("blocked_가_Y_이면_true_로_변환된다")
    void returnNullWhenBlockedIsNull() {
        // given
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .blocked(null)
                .build();

        // when
        SearchUserCommand searchUserCommand = searchUserRequestDto.toSearchUserCommand();

        // then
        assertThat(searchUserCommand.blocked()).isNull();
    }

    @Test
    @DisplayName("keyword_가_blank_이면_null_로_변환된다")
    void returnNullWhenKeywordIsBlank() {
        // given
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .username("   ")
                .build();

        // when
        SearchUserCommand searchUserCommand = searchUserRequestDto.toSearchUserCommand();

        // then
        assertThat(searchUserCommand.username()).isNull();
    }

    @Test
    @DisplayName("keyword_가_존재하면_trim_되어_설정된다")
    void trimKeywordWhenItExists() {
        // given
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .username("  hello  ")
                .build();

        // when
        SearchUserCommand searchUserCommand = searchUserRequestDto.toSearchUserCommand();

        // then
        assertThat(searchUserCommand.username()).isEqualTo("hello");
    }

    @Test
    @DisplayName("joinedFrom_과_joinedTo_는_그대로_전달된다")
    void preserveJoinedFromAndJoinedToValues() {
        // given
        LocalDate joinedFrom = LocalDate.of(2024, 1, 1);
        LocalDate joinedTo = LocalDate.of(2024, 12, 31);

        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .joinedFrom(joinedFrom)
                .joinedTo(joinedTo)
                .build();

        // when
        SearchUserCommand searchUserCommand = searchUserRequestDto.toSearchUserCommand();

        // then
        assertThat(searchUserCommand.joinedFrom())
                .isEqualTo(joinedFrom.atStartOfDay());

        assertThat(searchUserCommand.joinedTo())
                .isEqualTo(joinedTo.atTime(LocalTime.MAX));
    }

    @Test
    @DisplayName("blocked_는_대소문자_구분없이_동작한다")
    void shouldHandleBlockedCaseInsensitively() {
        // given
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .blocked("y")
                .build();

        // when
        SearchUserCommand searchUserCommand = searchUserRequestDto.toSearchUserCommand();

        // then
        assertThat(searchUserCommand.blocked()).isTrue();
    }
}