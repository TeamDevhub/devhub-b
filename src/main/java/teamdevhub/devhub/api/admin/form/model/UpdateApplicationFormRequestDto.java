package teamdevhub.devhub.api.admin.form.model;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.core.admin.form.port.in.command.UpdateApplicationFormCommand;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateApplicationFormRequestDto {

	@NotBlank(message = "제목은 필수입니다.")
	private String title;

	private String helpText;

	private boolean isUsed;

	private List<@NotBlank(message = "선택항목 내용은 필수입니다.") String> itemList;

	public UpdateApplicationFormCommand toCommand() {
		return UpdateApplicationFormCommand.builder()
				.title(this.title)
				.helpText(this.helpText)
				.isUsed(this.isUsed)
				.itemList(this.itemList)
				.build();
	}
}
