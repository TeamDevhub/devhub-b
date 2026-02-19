package teamdevhub.devhub.core.application.port.in.facade.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;

@Getter
@SuperBuilder
@NoArgsConstructor
public class ApplicationFormResponseDto {
	private String applicationFormGuid;
	private String title;
	private String typeCd;
	private String helpText;
	private String useYn;
	private String customYn;
	
	private String registrantGuid;
    private LocalDateTime registeredDate;
    private String modifierGuid;
    private LocalDateTime modifiedDate;
    
    public static ApplicationFormResponseDto fromDomain(ApplicationForm applicationForm) {
    	return ApplicationFormResponseDto.builder()
    			.applicationFormGuid(applicationForm.getApplicationFormGuid())
    			.title(applicationForm.getTitle())
    			.typeCd(applicationForm.getTypeCd())
    			.helpText(applicationForm.getHelpText())
    			.useYn(applicationForm.isUsed()?"Y":"N")
    			.customYn(applicationForm.isCustomized()?"Y":"N")
    			.build();
    }

}
