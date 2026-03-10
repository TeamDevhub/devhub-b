package teamdevhub.devhub.api.admin.form.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.core.admin.form.port.in.command.CreateApplicationFormCommand;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateApplicationFormRequestDto {

    @NotBlank(message = "타입은 필수입니다.")
    private String typeCd;

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    private String helpText;

    private List<@NotBlank(message = "선택항목 내용은 필수입니다.") String> itemList;

    public CreateApplicationFormCommand toCommand() {
        return CreateApplicationFormCommand.builder()
                .typeCd(this.typeCd)
                .title(this.title)
                .helpText(this.helpText)
                .itemList(itemList)
                .build();
    }
}
