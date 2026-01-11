package teamdevhub.devhub.small.adapter.in.vo;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.vo.PageResult;
import teamdevhub.devhub.adapter.in.vo.PageVo;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageVoTest {

    @Test
    void Page_로부터_PageVo_를_생성한다() {
        // given
        PageResult<String> pageResult = PageResult.of(
                List.of("data1", "data2"),
                1,
                10,
                25
        );

        // when
        PageVo<String> pageVo = PageVo.from(pageResult);

        // then
        assertThat(pageVo.getPage()).isEqualTo(1);
        assertThat(pageVo.getSize()).isEqualTo(10);
        assertThat(pageVo.getTotalPages()).isEqualTo(3);
        assertThat(pageVo.getTotalElements()).isEqualTo(25);
        assertThat(pageVo.isFirst()).isFalse();
        assertThat(pageVo.isLast()).isFalse();
    }

    @Test
    void 첫_페이지일_경우_first_는_true_이고_last_는_false_이다() {
        // given
        PageResult<String> pageResult = PageResult.of(
                List.of("data"),
                0,
                10,
                25
        );

        // when
        PageVo<String> pageVo = PageVo.from(pageResult);

        // then
        assertThat(pageVo.isFirst()).isTrue();
        assertThat(pageVo.isLast()).isFalse();
    }

    @Test
    void 마지막_페이지일_경우_last_는_true_이다() {
        // given
        PageResult<String> pageResult = PageResult.of(
                List.of("data"),
                2,
                10,
                25
        );

        // when
        PageVo<String> pageVo = PageVo.from(pageResult);

        // then
        assertThat(pageVo.isLast()).isTrue();
    }
}