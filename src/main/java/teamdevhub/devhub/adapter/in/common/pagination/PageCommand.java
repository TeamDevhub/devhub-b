package teamdevhub.devhub.adapter.in.common.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@AllArgsConstructor
public class PageCommand {
    private final int page;
    private final int size;

    public static PageCommand of(PageRequestDto vo) {
        return new PageCommand(vo.getPage(), vo.getSize());
    }
}