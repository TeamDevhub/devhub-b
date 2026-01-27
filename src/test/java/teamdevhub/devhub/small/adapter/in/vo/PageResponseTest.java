package teamdevhub.devhub.small.adapter.in.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.shared.web.model.response.PageResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageResponseTest {

    @Test
    @DisplayName("PageResult_로부터_PageVo_를_생성한다")
    void createPageVoFromPageResult() {
        // given
        PageResult<String> pageResult = PageResult.of(
                List.of("data1", "data2"),
                1,
                10,
                25
        );

        // when
        PageResponse pageResponse = PageResponse.from(pageResult);

        // then
        assertThat(pageResponse.getPage()).isEqualTo(1);
        assertThat(pageResponse.getSize()).isEqualTo(10);
        assertThat(pageResponse.getTotalPages()).isEqualTo(3);
        assertThat(pageResponse.getTotalElements()).isEqualTo(25);
        assertThat(pageResponse.isFirst()).isFalse();
        assertThat(pageResponse.isLast()).isFalse();
    }

    @Test
    @DisplayName("첫_페이지일_경우_first_는_true_이고_last_는_false_이다")
    void setFirstTrueAndLastFalseForFirstPage() {
        // given
        PageResult<String> pageResult = PageResult.of(
                List.of("data"),
                0,
                10,
                25
        );

        // when
        PageResponse pageResponse = PageResponse.from(pageResult);

        // then
        assertThat(pageResponse.isFirst()).isTrue();
        assertThat(pageResponse.isLast()).isFalse();
    }

    @Test
    @DisplayName("마지막_페이지일_경우_last_는_true_이다")
    void setLastTrueForLastPage() {
        // given
        PageResult<String> pageResult = PageResult.of(
                List.of("data"),
                2,
                10,
                25
        );

        // when
        PageResponse pageResponse = PageResponse.from(pageResult);

        // then
        assertThat(pageResponse.isLast()).isTrue();
    }
}