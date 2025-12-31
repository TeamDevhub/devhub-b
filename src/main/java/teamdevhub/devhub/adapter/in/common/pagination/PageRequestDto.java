package teamdevhub.devhub.adapter.in.common.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestDto {
    private int page = 0;
    private int size = 10;
}