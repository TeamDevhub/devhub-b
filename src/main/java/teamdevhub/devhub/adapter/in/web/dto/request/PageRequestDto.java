package teamdevhub.devhub.adapter.in.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestDto {

    private int page;

    private int size;
}