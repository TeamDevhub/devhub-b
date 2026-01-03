package teamdevhub.devhub.adapter.in.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PageVo {

    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private boolean first;
    private boolean last;

    public static PageVo from(Page<?> page) {
        return PageVo.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}