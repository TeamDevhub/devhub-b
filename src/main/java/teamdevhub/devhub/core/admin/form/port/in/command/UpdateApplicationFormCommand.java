package teamdevhub.devhub.core.admin.form.port.in.command;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateApplicationFormCommand {

	@NotBlank(message = "제목은 필수입니다.")
	private String title;

	private String helpText;

	private boolean isUsed;

	private List<String> itemList;
}
