package teamdevhub.devhub.adapter.in.web.converter;

import org.springframework.data.domain.Page;
import teamdevhub.devhub.adapter.in.vo.PageVo;

import java.util.List;
import java.util.function.Function;

public class PageConverter {

    public static PageVo toPageVo(Page<?> page) {
        return PageVo.from(page);
    }

    public static <T, R> List<R> toList(Page<T> page, Function<T, R> mapper) {
        return page.getContent()
                .stream()
                .map(mapper)
                .toList();
    }
}