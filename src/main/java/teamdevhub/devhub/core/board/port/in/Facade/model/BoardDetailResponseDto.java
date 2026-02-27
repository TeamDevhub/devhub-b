package teamdevhub.devhub.core.board.port.in.Facade.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDetailResponseDto {
	
	private BoardSummaryResponseDto boardSummaryResponseDto;
	private List<String> commentList;
}
