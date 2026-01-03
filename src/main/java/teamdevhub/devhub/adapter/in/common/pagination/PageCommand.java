package teamdevhub.devhub.adapter.in.common.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageCommand {

    private final int page;
    private final int size;

    private static final int MAX_SIZE = 100;

    public static PageCommand of(PageRequestDto pageRequestDto) {
        int safeSize = Math.max(1, Math.min(pageRequestDto.getSize(), MAX_SIZE));
        int safePage = Math.max(pageRequestDto.getPage(), 0);
        return new PageCommand(safePage, safeSize);
    }
}