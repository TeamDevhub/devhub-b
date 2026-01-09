package teamdevhub.devhub.small.adapter.in.vo;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import teamdevhub.devhub.adapter.in.vo.PageVo;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageVoTest {

    @Test
    void Page_로부터_PageVo_를_생성한다() {
        // given
        PageRequest pageRequest = PageRequest.of(1, 10);
        List<String> content = List.of("data1", "data2");

        Page<String> page = new PageImpl<>(
                content,
                pageRequest,
                25
        );

        // when
        PageVo pageVo = PageVo.from(page);

        // then
        assertThat(pageVo.getPage()).isEqualTo(1);
        assertThat(pageVo.getSize()).isEqualTo(10);
        assertThat(pageVo.getTotalPages()).isEqualTo(3);
        assertThat(pageVo.getTotalElements()).isEqualTo(25);
        assertThat(pageVo.isFirst()).isFalse();
        assertThat(pageVo.isLast()).isFalse();
    }

    @Test
    void 첫_페이지일_경우_first_는_true_이다() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<String> page = new PageImpl<>(
                List.of("data"),
                pageRequest,
                10
        );

        // when
        PageVo pageVo = PageVo.from(page);

        // then
        assertThat(pageVo.isFirst()).isTrue();
        assertThat(pageVo.isLast()).isTrue();
    }
}