package teamdevhub.devhub.core.project.domain.vo.requirement;

import lombok.Builder;

@Builder
public record ProjectRequirement(String projectRequirementGuid, String projectGuid, String positionCd, String levelCd, Integer capacity) {

}
