package teamdevhub.devhub.api.board.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import teamdevhub.devhub.core.board.port.in.command.SearchBoardCommand;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchBoardRequestDto {
	
	private String title;
	
	private String categoryCd;
	
	public SearchBoardCommand toCommand() {
		return SearchBoardCommand.builder()
				.title(title)
				.categoryCd(categoryCd)
				.build();
	}
	
}
