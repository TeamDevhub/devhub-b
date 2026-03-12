package teamdevhub.devhub.core.project.domain.vo.command;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import teamdevhub.devhub.core.admin.form.port.in.command.CreateApplicationFormCommand;
import teamdevhub.devhub.core.project.port.in.command.CreateProjectRequirementRequestCommand;

@Builder
public record UpdateProjectCommand(String userGuid, String username, String attachmentFileGuid, String imageFileGuid, String title, String category, String content, String recruitmentTypeCd, String progressTypeCd,
                                   String progressRegionCd, LocalDate recruitmentStartDate, LocalDate recruitmentEndDate, LocalDate progressStartDate, LocalDate progressEndDate,
                                   List<String> skillList, List<CreateProjectRequirementRequestCommand> positionList, List<String> applicationFormList, List<CreateApplicationFormCommand> additionalFormList) {}
