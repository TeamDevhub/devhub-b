package teamdevhub.devhub.small.adapter.in.web.converter;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import teamdevhub.devhub.adapter.in.vo.PageVo;
import teamdevhub.devhub.adapter.in.web.converter.PageConverter;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PageConverterTest {

    @Test
    void Page_객체를_PageVo로_변환한다() {
        List<String> content = List.of("A", "B", "C");
        Page<String> page = new PageImpl<>(content, PageRequest.of(0, 3), 10);

        PageVo pageVo = PageConverter.toPageVo(page);

        assertThat(pageVo.getPage()).isEqualTo(0);
        assertThat(pageVo.getSize()).isEqualTo(3);
        assertThat(pageVo.getTotalElements()).isEqualTo(10);
        assertThat(pageVo.getTotalPages()).isEqualTo(4);
        assertThat(pageVo.isFirst()).isTrue();
        assertThat(pageVo.isLast()).isFalse();
    }

    @Test
    void Page_객체의_content_를_매퍼로_변환하여_List_로_반환한다() {
        List<String> content = List.of("1", "2", "3");
        Page<String> page = new PageImpl<>(content, PageRequest.of(0, 3), 3);

        List<Integer> result = PageConverter.toList(page, Integer::parseInt);

        assertThat(result).containsExactly(1, 2, 3);
    }
}
