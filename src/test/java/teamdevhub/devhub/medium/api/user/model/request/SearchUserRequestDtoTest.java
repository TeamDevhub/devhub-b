package teamdevhub.devhub.medium.api.user.model.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.api.user.model.SearchUserRequestDto;
import teamdevhub.devhub.core.user.port.in.command.SearchUserCommand;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class SearchUserRequestDtoTest {

    @Test
    @DisplayName("blocked_가_Y_이면_blocked_는_true_로_변환된다")
    void blockedYConvertsToTrue() {
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
    @DisplayName("blocked_가_N_이면_blocked_는_false_로_변환된다")
    void blockedNConvertsToFalse() {
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
    @DisplayName("blocked_가_null_이면_blocked_는_null_이다")
    void blockedIsNullWhenBlockedIsNull() {
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
    @DisplayName("blocked_가_Y_N_이외의_값이면_blocked_는_null_이다")
    void blockedIsNullWhenBlockedIsInvalid() {
        // given
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .blocked("X")
                .build();

        // when
        SearchUserCommand searchUserCommand = searchUserRequestDto.toSearchUserCommand();

        // then
        assertThat(searchUserCommand.blocked()).isNull();
    }

    @Test
    @DisplayName("keyword_가_null_이면_keyword_는_null_이다")
    void keywordIsNullWhenKeywordIsNull() {
        // given
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .username(null)
                .build();

        // when
        SearchUserCommand searchUserCommand = searchUserRequestDto.toSearchUserCommand();

        // then
        assertThat(searchUserCommand.username()).isNull();
    }

    @Test
    @DisplayName("keyword_가_빈값이면_keyword_는_null_이다")
    void keywordIsNullWhenKeywordIsBlank() {
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
    @DisplayName("keyword_에_공백이_있으면_trim_되어_변환된다")
    void keywordIsTrimmed() {
        // given
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .username("  hello world  ")
                .build();

        // when
        SearchUserCommand searchUserCommand = searchUserRequestDto.toSearchUserCommand();

        // then
        assertThat(searchUserCommand.username()).isEqualTo("hello world");
    }

    @Test
    @DisplayName("joinedFrom_과_joinedTo_는_그대로_전달된다")
    void joinedFromAndJoinedToArePassedThrough() {
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
}