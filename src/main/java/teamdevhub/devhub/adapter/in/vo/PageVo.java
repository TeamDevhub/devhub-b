package teamdevhub.devhub.adapter.in.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageVo<T> {

    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;

    public static <T> PageVo<T> from(PageResult<T> pageResult) {
        return PageVo.<T>builder()
                .page(pageResult.page())
                .size(pageResult.size())
                .totalElements(pageResult.totalElements())
                .totalPages(pageResult.totalPages())
                .first(pageResult.first())
                .last(pageResult.last())
                .build();
    }
}