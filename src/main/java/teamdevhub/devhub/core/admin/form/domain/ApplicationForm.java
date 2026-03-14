package teamdevhub.devhub.core.admin.form.domain;


import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.admin.form.port.in.command.CreateApplicationFormCommand;

@Getter
@Builder
public class ApplicationForm {
	
	private String applicationFormGuid;
	
	private String typeCd;
	
	private String title;
	
	private String helpText;

	private boolean isCustomized;
	
	private boolean isUsed;
	
	public static ApplicationForm createCustomApplicationForm(CreateApplicationFormCommand createApplicationFormCommand, String applicationFormGuid) {
		return ApplicationForm.builder()
				.applicationFormGuid(applicationFormGuid)
				.typeCd(createApplicationFormCommand.getTypeCd())
				.title(createApplicationFormCommand.getTitle())
				.helpText(createApplicationFormCommand.getHelpText())
				.isCustomized(true)
				.isUsed(true)
				.build();
	}
}
