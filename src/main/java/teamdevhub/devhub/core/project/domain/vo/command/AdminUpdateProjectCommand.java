package teamdevhub.devhub.core.project.domain.vo.command;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record AdminUpdateProjectCommand(String title, String recruitmentTypeCd, String progressTypeCd,
        String progressRegionCd, LocalDate recruitmentStartDate, LocalDate recruitmentEndDate, LocalDate progressStartDate, LocalDate progressEndDate) {

}
