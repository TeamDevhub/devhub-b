package teamdevhub.devhub.api.application.model.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.api.web.model.response.PageResponseDto;

@Getter
@Builder
public class AdminProjectApplicationListResponseDto {

	private List<AdminProjectApplicationBasicResponseDto> applicantList;
	private PageResponseDto pagination;
}
