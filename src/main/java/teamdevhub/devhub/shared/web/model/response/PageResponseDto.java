package teamdevhub.devhub.shared.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.core.common.page.PageResult;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseDto {

    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;

    public static PageResponseDto from(PageResult<?> pageResult) {
        return PageResponseDto.builder()
                .page(pageResult.page())
                .size(pageResult.size())
                .totalElements(pageResult.totalElements())
                .totalPages(pageResult.totalPages())
                .first(pageResult.first())
                .last(pageResult.last())
                .build();
    }
}