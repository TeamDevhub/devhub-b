package teamdevhub.devhub.small.port.in.common.command;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.port.in.common.command.PageCommand;

import static org.assertj.core.api.Assertions.assertThat;

class PageCommandTest {

    @Test
    void 정상적인_page_와_size_로_PageCommand_를_생성한다() {
        // given, when
        PageCommand pageCommand = PageCommand.of(2, 20);

        // then
        assertThat(pageCommand.getPage()).isEqualTo(2);
        assertThat(pageCommand.getSize()).isEqualTo(20);
    }

    @Test
    void size_가_0이하면_1로_보정된다() {
        // given, when
        PageCommand pageCommand = PageCommand.of(0, 0);

        // then
        assertThat(pageCommand.getSize()).isEqualTo(1);
    }

    @Test
    void size_가_MAX_SIZE_를_초과하면_MAX_SIZE_로_보정된다() {
        // given, when
        PageCommand pageCommand = PageCommand.of(0, 1000);

        // then
        assertThat(pageCommand.getSize()).isEqualTo(100);
    }

    @Test
    void page_가_음수이면_0으로_보정된다() {
        // given, when
        PageCommand pageCommand = PageCommand.of(-1, 10);

        // then
        assertThat(pageCommand.getPage()).isEqualTo(0);
    }

    @Test
    void page_와_size_가_모두_음수이면_각_보정값으로_보정된다() {
        // given, when
        PageCommand pageCommand = PageCommand.of(-5, -10);

        // then
        assertThat(pageCommand.getPage()).isEqualTo(0);
        assertThat(pageCommand.getSize()).isEqualTo(1);
    }

}