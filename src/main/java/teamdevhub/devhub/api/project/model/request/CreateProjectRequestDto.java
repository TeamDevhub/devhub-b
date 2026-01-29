package teamdevhub.devhub.api.project.model.request;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.core.project.port.in.command.CreateProjectCommand;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectRequestDto {
    
	@NotBlank(message = "제목은 필수입니다")
    private String title;
    
	@NotBlank(message = "카테고리는 필수입니다")
    private String category;
    
	@NotBlank(message = "상세내용은 필수입니다")
    private String content;
    
	@NotBlank(message = "모집방식은 필수입니다")
    private String recuritmentTypeCd;
    
	@NotBlank(message = "진행방식은 필수입니다")
    private String progressTypeCd;
    
    private String progressRegionCd;
    
    @NotBlank(message = "모집시작일은 필수입니다")
    private LocalDateTime recuritmentStartDate;
    
    @NotBlank(message = "모집종료일은 필수입니다")
    private LocalDateTime recuritmentEndDate;
    
    @NotBlank(message = "진행시작일은 필수입니다")
    private LocalDateTime progressStartDate;
    
    @NotBlank(message = "진행종료일은 필수입니다")
    private LocalDateTime progressEndDate;
    
    @NotNull(message = "기술스택은 필수입니다")
    @Size(min = 1, message = "기술스택은 최소 1개 이상 선택해야 합니다")
    private List<String> skillList;
    
    @NotNull(message = "모집인원은 필수입니다")
    @Size(min = 1, message = "모집인원은 최소 1개 이상 선택해야 합니다")
    private List<CreateProjectRequirementRequestDto> positionList;
    
    public CreateProjectCommand toCommand(String userGuid) {
    	return CreateProjectCommand.builder()
    			.userGuid(userGuid)
    			.title(this.title)
    			.category(this.category)
    			.content(this.content)
    			.recuritmentTypeCd(this.recuritmentTypeCd)
    			.progressTypeCd(this.progressTypeCd)
    			.progressRegionCd(this.progressRegionCd)
    			.recuritmentStartDate(this.recuritmentStartDate)
    			.recuritmentEndDate(this.recuritmentEndDate)
    			.progressStartDate(this.progressStartDate)
    			.progressEndDate(this.progressEndDate)
    			.skillList(this.skillList)
    			.positionList(this.positionList)
    			.build();
    }

}
