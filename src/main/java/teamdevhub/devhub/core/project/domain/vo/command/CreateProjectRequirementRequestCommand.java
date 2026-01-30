package teamdevhub.devhub.core.project.domain.vo.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.core.project.port.in.command.CreateProjectRequirementCommand;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectRequirementRequestCommand {

	@NotBlank(message = "모집포지션은 필수입니다")
	private String position;
	
	@NotBlank(message = "숙련도는 필수입니다")
	private String level;
	
	@NotBlank(message = "모집인원수는 필수입니다")
	private Integer capacity;
	
	public CreateProjectRequirementCommand toCommand() {
		return CreateProjectRequirementCommand.builder()
				.position(this.position)
				.level(this.level)
				.capacity(this.capacity)
				.build();
	}
}
