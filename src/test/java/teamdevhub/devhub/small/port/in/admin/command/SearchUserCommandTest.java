package teamdevhub.devhub.small.port.in.admin.command;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;
import teamdevhub.devhub.adapter.in.admin.user.dto.SearchUserRequestDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SearchUserCommandTest {

    @Test
    void blocked_가_Y이면_true_로_변환된다() {
        // given
        SearchUserRequestDto requestDto = SearchUserRequestDto.builder()
                .blocked("Y")
                .build();

        // when
        SearchUserCommand command = SearchUserCommand.fromSearchUserRequestDto(requestDto);

        // then
        assertThat(command.getBlocked()).isTrue();
    }

    @Test
    void blocked_가_N이면_false_로_변환된다() {
        // given
        SearchUserRequestDto requestDto = SearchUserRequestDto.builder()
                .blocked("N")
                .build();

        // when
        SearchUserCommand command = SearchUserCommand.fromSearchUserRequestDto(requestDto);

        // then
        assertThat(command.getBlocked()).isFalse();
    }

    @Test
    void blocked_가_null_이면_null_로_유지된다() {
        // given
        SearchUserRequestDto requestDto = SearchUserRequestDto.builder()
                .blocked(null)
                .build();

        // when
        SearchUserCommand command = SearchUserCommand.fromSearchUserRequestDto(requestDto);

        // then
        assertThat(command.getBlocked()).isNull();
    }

    @Test
    void keyword_가_blank_이면_null_로_변환된다() {
        // given
        SearchUserRequestDto requestDto = SearchUserRequestDto.builder()
                .keyword("   ")
                .build();

        // when
        SearchUserCommand command = SearchUserCommand.fromSearchUserRequestDto(requestDto);

        // then
        assertThat(command.getKeyword()).isNull();
    }

    @Test
    void keyword_가_존재하면_trim_되어_설정된다() {
        // given
        SearchUserRequestDto requestDto = SearchUserRequestDto.builder()
                .keyword("  hello  ")
                .build();

        // when
        SearchUserCommand command = SearchUserCommand.fromSearchUserRequestDto(requestDto);

        // then
        assertThat(command.getKeyword()).isEqualTo("hello");
    }

    @Test
    void joinedFrom_과_joinedTo_는_그대로_전달된다() {
        // given
        LocalDateTime joinedFrom = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime joinedTo = LocalDateTime.of(2024, 12, 31, 23, 59);

        SearchUserRequestDto requestDto = SearchUserRequestDto.builder()
                .joinedFrom(joinedFrom)
                .joinedTo(joinedTo)
                .build();

        // when
        SearchUserCommand command = SearchUserCommand.fromSearchUserRequestDto(requestDto);

        // then
        assertThat(command.getJoinedFrom()).isEqualTo(joinedFrom);
        assertThat(command.getJoinedTo()).isEqualTo(joinedTo);
    }

    @Test
    void blocked_가_대소문자_구분없이_동작한다() {
        // given
        SearchUserRequestDto requestDto = SearchUserRequestDto.builder()
                .blocked("y")
                .build();

        // when
        SearchUserCommand command = SearchUserCommand.fromSearchUserRequestDto(requestDto);

        // then
        assertThat(command.getBlocked()).isTrue();
    }
}